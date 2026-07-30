# SoftMan — Architectural Plan

> Reframing note, 2026-07-30. Goal: finish a single-player desktop game first, but build the core so it can later back an asynchronous online multiplayer league (teams controlled by real players, matches resolved once a week in real time).

Prompt (`Claude Opus 5` via `GitHub Copilot`):

> Perfect, before diving into shaping current solution, I would like to do some big picture reframing. For now, I want to build a single-player desktop game, just to finish something first. But I would like to build its core in a way that would allow re-using it for possible future online multiplayer experience (where the teams are controlled by real players and games are being played like once a week in real time). What considerations should be taken into account? Is Java suitable for such ambition?

## Is Java suitable?

Yes — and for the multiplayer model described, it's close to ideal.

"Teams controlled by real players, matches resolved once a week" is **asynchronous, turn-based multiplayer** (Hattrick / OOTP online league / play-by-mail), not real-time netcode. There's no latency budget, no client prediction, no rollback, no lockstep. The server just needs to: accept orders before a deadline, run a scheduled tick, persist results, and serve state. That's the JVM's home turf — scheduled jobs, transactional persistence, long-running server processes, virtual threads for cheap concurrency.

The only Java-specific caveat: **JavaFX cannot exist on the server**. That's the single hardest constraint, and the current code violates it in several places.

---

## The one principle everything else follows from

> **The core must be a headless, deterministic, authoritative library that knows nothing about who is looking at it.**

If the simulation engine can run with no display, no user, no `main()`, and produce identical results from identical inputs, then "single-player desktop" and "weekly online league" are just two different *drivers* of the same engine. If it can't, it will have to be rewritten.

---

## Where the current code blocks that

| Blocker | Where | Why it matters |
|---|---|---|
| JavaFX types inside game logic | `MatchSimulator` takes a `TextArea`; `AssociationManager` holds a `ProgressIndicator`; `Club` holds a `javafx.scene.paint.Color`; `League.mockPlay*` takes a `TextArea` | Core cannot be loaded on a headless server at all |
| Global singletons = exactly one world | `AssociationManager.getInstance()`, `ClockManager`, `GameDBManager`, `UserManager` | A server hosts *many* leagues concurrently. Singleton world state is the #1 thing that's expensive to retrofit |
| `testMode` flag | `AssociationManager` | Symptom of the same coupling — logic branching on "is there a UI?" |
| Unseeded `new Random()` scattered around | `MatchSimulator`, `SimUtils`, `PlayerAttributes`, `Team.randomizeLineup` | No reproducibility → no replays, no audit, no "why did my ace get shelled?", no deterministic tests |
| UI mutates domain directly | `LineupTab` → builds a `Lineup` and saves it | Server must validate intent, not accept objects |
| Active-record persistence | `AbstractDBEntity.persist()` on every entity | Entities save themselves whenever, no transaction boundary, no unit of work. Fine locally, dangerous with concurrent writers |
| `UserManager` conflates identity with UI focus | `activeClub` vs `focusedClub`/`focusedTeam` + listeners | "Who am I" (server concept) and "what am I looking at" (client concept) must split |
| Auto-increment `long` IDs per local DB | all ORM entities | IDs collide across worlds; no stable identity across save/transfer |

---

## Considerations, in rough order of "expensive to retrofit"

**1. Instance-scoped world, not singletons.**
A `World` (or `GameWorld`) object owns clock, leagues, clubs, players, RNG. Everything that currently calls `AssociationManager.getInstance()` receives a `World` instead. This is the big one. Locally you have one; on a server you have N.

**2. Seeded, injected randomness.**
One `Random`/`RandomGenerator` per world, seeded from a stored long. Pass it down; never `new Random()` in a constructor. `PlayerAttributes` rolling its own stats in its no-arg constructor is a good example of what to move to a factory. Payoff: deterministic tests for `MatchSimulator` (currently untested), replayable matches, and the ability to re-run a match from `(seed, matchId)` instead of storing every play.

**3. Commands in, events out.**
Instead of the UI mutating a `Lineup`, it submits `SetLineup(teamId, spots[])`. The core validates and either rejects it or applies it. In Java 25 this is very natural with sealed interfaces + records:

```java
sealed interface Command permits SetLineup, SignPlayer, SetTraining { }
record SetLineup(long teamId, List<LineupSpot> spots) implements Command { }
```

Same shape works for the desktop game (called directly) and for the server (deserialized from JSON). Symmetrically, simulation emits events (`RunScored`, `MatchFinished`) instead of appending strings to a `TextArea` — the desktop client renders them as play-by-play text, the server stores them.

**4. Explicit tick / advance-time boundary.**
Right now time advancement is entangled with `SimulationService` (a JavaFX `Service`), UI refresh, and a `ProgressIndicator`. Extract a pure `world.advanceTo(date)` that returns results. The JavaFX `Service` becomes a thin wrapper; the server's scheduler calls the same method. Also: matchday must be data (a scheduled timestamp), not "the user clicked Next Day".

**5. Orders + deadline model.**
Even in single-player, model it as: players submit orders → deadline passes → tick resolves. If you build "the human acts, then time moves" you'll have to invert it later. If you build "orders accumulate, tick resolves them", weekly multiplayer is nearly free — and AI managers become just another order source.

**6. Serialization = save/load = network transport.**
This is the big lever: **properly implementing save/load gets you ~80% of network-readiness for free.** Both require that the entire world state be reconstructible from data alone, with no hidden state in static fields or UI. The current `dropExisting = true` + hardcoded `gameId = "test"` means this has never been proven. Fixing save/load *first* validates the whole design.

**7. Identity and ownership.**
Introduce a `ManagerId` / `Principal` concept now, even if single-player always has exactly one and everything else is AI. Teams reference "who controls me". Then `activeClub` stops being special and becomes "the club whose manager is me". Authorization checks (`can this manager set that lineup?`) belong in the core, not the UI.

**8. Stable IDs.**
Move to UUIDs, or world-scoped composite IDs. Auto-increment per-SQLite-file will bite the moment there are two worlds or any data exchange.

**9. Persistence abstraction.**
No need to abandon SQLite or OrmLite now, but stop letting entities persist themselves. A repository interface per aggregate lets the desktop use SQLite and the server later use Postgres without touching game logic. Also gives a transaction boundary per tick, which is needed with concurrent players.

**10. Anti-cheat / authority (later, but design for it).**
Server simulates, clients render. Never trust a client-computed result. This falls out automatically if the core is authoritative and clients only send commands.

---

## Suggested module layout

Split the Maven build into modules. This mechanically enforces the rules — JavaFX *cannot* be accidentally imported into core:

```
softman-parent (pom)
├── softman-core        # domain, rules, MatchSimulator, commands, events
│                       # deps: NONE beyond slf4j/commons. No JavaFX. No JDBC.
├── softman-persistence # repositories, OrmLite/SQLite impl
├── softman-app         # single-player wiring: world lifecycle, save/load, AI managers
├── softman-desktop     # JavaFX client (current gui/ package)
└── softman-server      # LATER: scheduler + HTTP API over softman-core
```

`softman-core` having zero JavaFX and zero JDBC dependencies is the litmus test. Everything else is negotiable.

Adding `module-info.java` (JPMS) on top would harden this further and fix the *"Unsupported JavaFX configuration: classes were loaded from unnamed module"* warning already appearing in the build — but that's optional polish, do it after the split.

---

## What NOT to do now

The goal is to *finish something first*. So deliberately skip:

- ❌ Any networking, REST API, auth, or accounts
- ❌ Event sourcing / CQRS
- ❌ Postgres, Docker, cloud anything
- ❌ A DI framework (constructor injection by hand is enough at this size)
- ❌ Protobuf/gRPC or a custom wire protocol
- ❌ Microservices of any description

These are all **cheap to add later** precisely *because* they sit outside the core. The expensive-to-retrofit items are the ones above: instance-scoped world, seeded RNG, commands, headless core, real save/load.

---

## Realistic order of work

1. **Split the Maven modules**, move `logic/` + `utils/` into `softman-core`. Compilation will fail — that failure list *is* the coupling report.
2. **Purge JavaFX from core**: `TextArea` → event/listener interface; `Color` → hex string; `ProgressIndicator` → progress callback.
3. **Kill the singletons**: `AssociationManager` becomes `World`, passed explicitly. Delete `testMode`.
4. **Seed the RNG** per world; add the first real `MatchSimulator` tests now that it's possible.
5. **Make save/load actually work** (drop `dropExisting = true`, real `gameId`, repair `Match.getMatchFromDB()`). Prove state round-trips.
6. **Introduce commands** for the handful of player actions that exist (set lineup, advance time).
7. *Then* build out the missing manager pillars — training, market, finances — on top of a shape that won't need rewriting.

Steps 1–5 also happen to fix most of what's currently broken in the codebase, so this isn't a detour from "finish the single-player game" — it's the same road.

---
---

# Tech Stack Review — SQLite & JavaFX

> Follow-up question, 2026-07-30: assuming a single-player desktop app in Java, are SQLite and JavaFX wise and sustainable choices?

## Short verdict

**JavaFX: keep it.** It's a reasonable, sustainable choice for this kind of game, and it's already half-built.
**SQLite: keep the engine, drop the usage pattern.** The problem isn't SQLite — it's using a live ORM as the game's working memory.
**The riskier dependencies are actually the UI add-ons, not either of these.**

---

## 1. Persistence — the real issue isn't SQLite

Ask the shape question first: **is the world state relational, or is it an object graph?**

It's an object graph. And it's *tiny*:

| Entity | Count |
|---|---|
| Clubs | 16 |
| Players | ~320 (16 × 20) |
| Matches per season | 112 (8 teams × 28 rounds) |
| Plays per match | ~50–70 |

The entire world comfortably fits in a few MB of heap. Football Manager, OOTP, and every game of this genre hold the world in memory and snapshot it — none of them run the simulation against a live SQL database.

### What's actually wrong today

It isn't SQLite. It's that OrmLite is used as **active-record working memory**: `AbstractDBEntity.persist()` is called on individual entities *during* simulation, there's no transaction boundary, no unit of work, and `dropExisting = true` means the DB is thrown away every launch anyway. You pay the full cost of a database and get none of the benefits.

### Three viable directions

| Approach | Fit |
|---|---|
| **A. In-memory world + snapshot file** (JSON via Jackson, or binary) | ⭐ Best fit for v1. Matches the `World` object above exactly. Save = serialize; load = deserialize. Trivially testable. And serialization work carries straight over to network transport |
| **B. SQLite as a save-file *format*** — write the whole world in one transaction on save, read it all on load | Reasonable if crash-resistance and inspectability matter (a save can be opened in DB Browser). Still in-memory at runtime |
| **C. SQLite as a live queried store** (current approach) | Only justified for **historical archives** — career stats across 30 seasons, where data grows unbounded and ad-hoc queries are wanted |

**Recommendation: A now, with C added later, only for the stats archive.** Keep the world in memory, snapshot it, and if season-history queries ever become a real feature, append finished-season data to a SQLite archive that the simulation never reads back.

One relevant detail: **SQLite doesn't carry into multiplayer anyway** — single-writer locking makes it a poor server database. So a repository abstraction (item 9 above) is needed regardless, and that abstraction is what makes this decision cheap to revisit.

### If a DB is kept, reconsider OrmLite

OrmLite 6.1 is old and slowly maintained. Better options if going DB-backed:
- **plain JDBC + Java records** — genuinely fine at this scale, zero magic, no annotations
- **JDBI 3** — thin, modern, record-friendly
- **jOOQ** — excellent for typed SQL and a possible later move to Postgres

Skip Hibernate/JPA entirely — massive complexity for 320 players.

---

## 2. JavaFX — sustainable, with one strategic caveat

**Why it's a fine choice:**
- Actively maintained (Gluon), regular releases, LTS track — not a dead technology
- Genuinely good at **data-dense desktop UI**: `TableView`, sorting, virtualization, bindings. That's ~80% of a manager game
- Native Java, no bridge, no IPC, no second language
- CSS-ish theming and `jpackage` for real installers
- Working tabs, tiles, tables and a lineup editor already exist. Rewriting the UI now is exactly the kind of restart that kills side projects

**Honest downsides:**
- Small and shrinking community; fewer libraries, fewer answers
- Distribution needs `jlink`/`jpackage` — solvable, but a chunk of work
- No hot reload; UI iteration is slow (UI is built programmatically, so every tweak is a recompile)
- CSS subset is idiosyncratic

### The one thing worth thinking hard about

Given the multiplayer ambition: **a web UI is the only frontend that carries over to online play.** Every online manager game (Hattrick, OOTP online leagues, browser football managers) is a web app — because the UI is tables, forms and dashboards, which the web does better than anything, and because it's the same UI for one player or a thousand.

Building a local HTTP server (Javalin/Helidon, ~50 lines) serving a web frontend would give:
- one UI codebase for desktop and future multiplayer
- vastly faster iteration
- but: a second tech stack to learn/maintain, a worse "it's an app" feel, and discarded existing work

**Call: don't switch now.** The module split above makes the UI a *driver*, not the architecture — so this becomes a genuinely reversible decision. Finish the single-player game on JavaFX. If and when multiplayer becomes real, `softman-desktop` is one client and a web client is another, both talking to the same core. That's the whole point of doing the split first.

If JavaFX is kept, two worth-it upgrades:
- **AtlantaFX** instead of BootstrapFX — modern, actively maintained themes, dark mode
- Consider FXML + Scene Builder for *static* layouts (tabs, forms); keep programmatic construction for dynamic content like lineup rows

---

## 3. The dependencies that should actually be worried about

More urgent than either question above — the current stack leans on several effectively-abandoned libraries:

| Dependency | Version | Concern |
|---|---|---|
| **FontAwesomeFX** | 9.1.2 | Hosted on Bitbucket, no releases in years; the README links a Bitbucket branch that may not survive. **Highest risk** |
| **BootstrapFX** | 0.4.0 | No release in years; styling is dated |
| **Medusa** | 16.0.0 | Sporadic maintenance, niche (gauges only) |
| **OrmLite** | 6.1 | Slow maintenance |
| ControlsFX | 11.2.3 | ✅ Actively maintained — fine |
| sqlite-jdbc | 3.51.1.0 | ✅ Actively maintained — fine |

These are far likelier to break a Java 26/27 upgrade than JavaFX or SQLite are. FontAwesomeFX in particular is used only for menu icons — cheap to replace with **Ikonli** (actively maintained, same idea, many icon packs).

---

## Concrete recommendation

1. **Keep JavaFX.** Revisit only if/when multiplayer becomes real — the module split makes that cheap.
2. **Keep SQLite available, but stop simulating against it.** Move to an in-memory `World` + snapshot save file. This *is* step 5 of the order of work above, and it simplifies rather than complicates it.
3. **Replace FontAwesomeFX with Ikonli**, and BootstrapFX with AtlantaFX. Small, contained, removes the two most fragile dependencies.
4. **Defer the OrmLite decision** — put repositories behind interfaces first (item 9), then swapping OrmLite for plain JDBC or dropping it entirely becomes a one-module change.

Net effect: nothing in the stack needs replacing to reach a finished single-player game. The genuinely load-bearing change is *how* SQLite is used, not *whether*.
