# SoftMan — Backlog

Tasks deferred for later. Remove an entry once it is done.

If a finished task changes anything `AGENTS.md` states (modules, dependencies, commands, conventions,
GUI decisions), update `AGENTS.md` in the same change. Entries marked **AGENTS.md:** name the known spot.

## Architecture

Carried over from the archived [PLAN.md](./archive/PLAN.md) (headless core for a future async multiplayer
server). Rationale and the "what not to do now" list live there.

- [ ] **Instance-scoped `World` instead of singletons** — `AssociationManager` becomes a `World` passed
  explicitly (clock, leagues, clubs, players, RNG); delete `testMode`. Conflicts with the current
  `getInstance()` convention in `AGENTS.md` — decide before starting. **AGENTS.md:** replace the
  singleton convention with how the `World` is obtained and passed.
- [ ] **Seeded, injected RNG** — one `RandomGenerator` per world, seeded from a stored long; remove the
  `new Random()` calls in `MatchSimulator`, `SimUtils`, `StatsUtils`, `PlayerAttributes`, `Team`,
  `RandomSubstitutionStrategy` and the factories. Enables replays from `(seed, matchId)`.
  **AGENTS.md:** add a convention — no `new Random()`, use the world RNG.
- [ ] **`MatchSimulator` / `SimUtils` / `StatsUtils` tests** — deterministic once the RNG is seeded.
- [ ] **Explicit tick in core** — a pure `advanceTo(date)` returning results; `SimulationService` becomes a
  thin wrapper instead of `AssociationManager` delegating to `ISimulationRunner` (or `plainAdvanceToNextDay`
  in test mode). Matchday stays data, not "the user clicked Next Day".
- [ ] **Commands in, events out** — sealed `Command` records (`SetLineup`, advance time, …) validated by
  core; `LineupTab` currently builds a `Lineup` and sets it directly. Simulation emits events instead of
  text lines through `IMatchReporter`. **AGENTS.md:** add a convention — the GUI changes game state
  only through commands.
- [ ] **Orders + deadline model** — orders accumulate, the tick resolves them; AI managers are just another
  order source.
- [ ] **Identity and ownership** — a `ManagerId` that teams reference; split "who am I" (`activeClub`) from
  "what am I looking at" (`focusedClub`/`focusedTeam`) in `UserManager`; authorization checks in core.
- [ ] **Reconsider OrmLite (optional)** — slowly maintained; plain JDBC + records or JDBI 3 are the
  candidates. Now a `softman-db`-only change behind `IGameRepository`. **AGENTS.md:** `softman-db` row
  in the Modules table.
- [ ] **JPMS (optional)** — `module-info.java` per module; fixes the "unnamed module" JavaFX warning.
  **AGENTS.md:** Modules rules (exported packages) and Conventions.
- [ ] **Manager pillars** — training, transfer market, finances; build on top of the items above.

## Match substitutions

- [ ] **DP/FLEX rules** — DP playing defense (for FLEX or another player → OPO), FLEX batting for DP,
  lineup dropping to 9. Currently the DP spot is offense-only and FLEX defense-only.
- [ ] **Pitcher-specific rules** — restrictions on a removed pitcher returning to the circle.
- [ ] **Courtesy runners** for pitcher/catcher (do not count as substitution).
- [ ] **Offensive substitutions other than PH/PR** — currently only the upcoming batter or a runner
  can be replaced while batting.
- [ ] **Systematic substitution strategy** — new `ISubstitutionStrategy` beside `RandomSubstitutionStrategy`:
  pull the pitcher after a series of hits/walks (per-game `PlayerStats` of the current pitcher), pull a
  batter after 2–3 failed at-bats, pick replacements by attributes instead of at random.
- [ ] **Per-team strategy** — `MatchSimulator` uses one strategy for both teams; allow a different one per
  team (e.g. user-chosen coaching style for auto-simulated games).

## Match simulation

- [ ] **One simulator per match** — `ScheduleRowTile` creates its own `MatchSimulator` (on every refresh),
  independent from the one in `MatchTab`, so game state (inning, outs, runners) diverges between them.
- [ ] **Simulator state is not persisted** — after loading a saved game, an `ACTIVE` match restarts
  from inning 1 with empty bases.
- [ ] **Match lineup is the team's default lineup** — `League` passes `team.getDefaultLineup()` into
  `Match`, so all matches of a team share one `Lineup` instance. In-game substitutions stay visible in
  `LineupTab` after the game until the next game's `setUp`, and past matches don't keep their own lineups.
- [ ] **Starter stats written into default lineup records** — `Lineup.setUp` sets the match stats on the
  default lineup's `PlayerRecord`s (consequence of the shared lineup above).
- [ ] **`Team.randomizeLineup` can put the DP into spot 10** — positions are drawn at random for all
  batting spots incl. the FLEX spot, so the DP may land in spot 10 and never bat. DP must be in spots
  1–9 and spot 10 must hold a defensive position (the FLEX).
- [ ] **`Team.randomizeLineup` crashes on small rosters** — the substitutes loop calls
  `availablePlayers.remove(rand.nextInt(size))` without checking the list is non-empty, so a roster too
  small to fill the bench throws `IllegalArgumentException`. The loop condition
  (`i <= SUBSTITUTES || i < availablePlayers.size()`) is also off and keeps drawing past spot 8.
- [ ] **`LineupTile.checkLineup` misses bench duplicates** — it only checks position players against each
  other; a starter also listed as a substitute, or one player listed twice on the bench, is accepted.
  Harmless in-game (`Lineup.getAvailableReplacements` filters them) but the lineup is wrong.
- [ ] **Old saves may contain duplicate in-game players** — produced by the removed
  `evaluateRandomSubstitution` (same bench player picked twice). `Lineup.substitutePlayer` replays them
  unchecked on load; finished games keep the old box scores.

## GUI

- [ ] **Migrate to AtlantaFX 3.0.0** — released 2026-09-24; archive/GUI.md locks 2.1.0. Check the changelog for
  breaking changes (theme classes, `Styles` constants, looked-up `-color-*` vars), bump in root `pom.xml`,
  run `GuiSmokeTest`, then update the version in archive/GUI.md. **AGENTS.md:** GUI work section, if
  theme names or looked-up vars change.
- [ ] **`ProgressIndicatorUtil` is unused** — third-party gist with inline black/white styles; delete it or
  restyle on theme vars if a dimming overlay is wanted (the live spinner is in `Softman.setupStage`).
- [ ] **`FocusFrame` goes stale after New/Load game** — its club/team combos are built once in the
  singleton constructor and are not refreshed by `MainLayout.setUp()`.
- [ ] **Theme preference** — the dark-theme choice is not persisted and the app always starts light;
  optionally seed from `Platform.getPreferences().getColorScheme()`. **AGENTS.md:** the Theme bullet
  ("`NordLight` by default").
- [ ] **Dev tools (GUI plan 3.3, optional)** — dev-only DevToolsFX or Scenic View for live scene-graph inspection.
  **AGENTS.md:** UI libraries bullet, and Commands if it adds a launch profile.
- [ ] **Minor Phase 2 visual follow-ups** — noted by the owner at review, to be specified.

## Agent instructions

- [ ] `AGENTS.md` still holds reference to archived `docs\archive\PLAN.md` mentioning "locked-in technology decisions" - those need to me moved elsewhere.
