# AgentCreed Backlog

<!-- ↓↓↓ agentcreed ↓↓↓ -->

This file is used to collect tasks for the project codebase discovered during development (e.g. refactoring, duplicated or unused code, bugs, missing tests, missing or outdated documentation, changes out of the current task scope). Findings about agent instructions belong in `SUGGESTIONS.md`.

This file holds pending tasks only, as a numbered list. Resolved items are deleted, never marked as done. When resolved items are deleted, the remaining items are renumbered.

<!-- ↑↑↑ agentcreed ↑↑↑ -->

Pending work, known bugs and deferred tasks. Entries marked **ARCHITECTURE.md:** name the sections to update once the item is done. Open `TODO` comments in the code are further pending work.

## Bugs

1. Unexplained stats bug, observed once (2026-10-31): after a simulated match with a substitution (starter replaced by a bench player in the same spot), the saved and reloaded game credited both stats lines of that match to the starter; the substitute had none. The play-by-play stats printout was correct. Not reproducible: tests of a single match, 30 full simulated games, and a save/load round trip all credited stats correctly. The user will start a new simulation and watch whether it recurs. If it does, capture the save before and after loading, and check whether the starter's `PlayerStats` rows carry the substitute's `playerId`.
2. Starting or loading a game has no single routine (existing `TODO unify actions performed upon starting new game` in `Softman.setupStage` and `MainLayout.setUp`). After New game or Load game, the "Current date" label (`ActionFrame`), the `FocusFrame` club and team selectors (built once in the singleton constructor), the `ClubTab` league table and the schedule are not refreshed. Fix: one routine used by startup, New game and Load game.
3. `Softman.setupStage` calls `AssociationManager.nextDay()` at startup. This simulates the first day (after a confirmation prompt) and is also the only thing that initializes the "Current date" label. Decide whether startup should keep simulating day 1 or just set the date label.
4. Autosave is bypassed by the window close button — saving happens only in `Softman.closeIfConfirmed()` (Esc / Game → Exit). There is no `setOnCloseRequest` and no `Application.stop()` override, so closing via the title bar loses the session.
5. Second season is scheduled in the past — `League.scheduleMatches()` always starts from the constant `LEAGUE_START = LocalDate.of(START_YEAR, 4, 1)`, so leagues created by `recreateLeagues()` in year N+1 get 2025 match dates.
6. `League.includeMatchIntoStandings()` uses a `do/while` with an `index > 9` guard that can throw `IndexOutOfBounds` for leagues with >10 teams and never short-circuits correctly.
7. `League.mockPlayLeague/mockPlayRound/mockPreviewCurrentRound` just append `"MOCK BROKEN AND HOPEFULLY DEAD"`; still reachable from `StandingsTab` buttons.
8. Likely attribute bug — `PlayerAttributes.getFieldingSkill()` averages `fieldingReach + gloveControl + pitchVariety`; `pitchVariety` looks like a copy/paste error (probably `throwControl`, which is instead used in `getPitchingSkill()`).
9. Broken format string on an error path — `MatchSimulator.wrapUpMatch()` calls `ErrorUtils.raise(String.format("Unknown leagueId %d", leagueId))` with a `UUID`, which throws `IllegalFormatConversionException` instead of the intended assertion.
10. Table name concatenated into SQL in `SqliteNameSource.getRandomName()` — internal constants today, but the pattern is injection-prone.
11. `@SuppressWarnings("unchecked")` in `GameDatabase.dao()` and `Lineup.positionPlayers` (generic array) — flagged TODOs.

## Architecture

Carried over from the archived [2026-architecture-changes.md](../docs/archive/2026-architecture-changes.md) (headless core for a future async multiplayer server). Rationale and the "what not to do now" list live there.

 1. **Instance-scoped `World` instead of singletons** — `AssociationManager` becomes a `World` passed explicitly (clock, leagues, clubs, players, RNG); delete `testMode` (exists only to bypass JavaFX in tests). Conflicts with the current `getInstance()` convention — decide before starting. **ARCHITECTURE.md:** Cross-cutting patterns (Façade, Singletons everywhere), Conventions (Singletons).
 2. **Seeded, injected RNG** — one `RandomGenerator` per world, seeded from a stored long; remove the `new Random()` calls in `MatchSimulator`, `SimUtils`, `StatsUtils`, `PlayerAttributes`, `Team`, `RandomSubstitutionStrategy` and the factories. Enables replays from `(seed, matchId)`. **ARCHITECTURE.md:** Cross-cutting patterns (new RNG bullet), Match simulation model; add the "no `new Random()`, use the world RNG" rule to Conventions.
 3. **`MatchSimulator` / `SimUtils` / `StatsUtils` tests** — deterministic once the RNG is seeded. **ARCHITECTURE.md:** Tests.
 4. **Explicit tick in core** — a pure `advanceTo(date)` returning results; `SimulationService` becomes a thin wrapper instead of `AssociationManager` delegating to `ISimulationRunner` (or `plainAdvanceToNextDay` in test mode). Matchday stays data, not "the user clicked Next Day".
 5. **Commands in, events out** — sealed `Command` records (`SetLineup`, advance time, …) validated by core; `LineupTab` currently builds a `Lineup` and sets it directly. Simulation emits events instead of text lines through `IMatchReporter`. **ARCHITECTURE.md:** Cross-cutting patterns (Ports & adapters), Match simulation model; add the "GUI changes game state only through commands" rule to Conventions.
 6. **Orders + deadline model** — orders accumulate, the tick resolves them; AI managers are just another order source.
 7. **Identity and ownership** — a `ManagerId` that teams reference; split "who am I" (`activeClub`) from "what am I looking at" (`focusedClub`/`focusedTeam`) in `UserManager`; authorization checks in core.
 8. **Reconsider OrmLite (optional)** — slowly maintained; plain JDBC + records or JDBI 3 are the candidates. Now a `softman-db`-only change behind `IGameRepository`. **ARCHITECTURE.md:** Tech Stack (Persistence row), module table (`softman-db` row), Package map (`db` row), Persisted tables.
 9. **JPMS (optional)** — `module-info.java` per module; fixes the "unnamed module" JavaFX warning. **ARCHITECTURE.md:** Tech Stack (Language row), module table, Conventions.
10. **Manager pillars** — training, transfer market, finances; build on top of the items above. **ARCHITECTURE.md:** Feature Status.

## Match substitutions

 1. **DP/FLEX rules** — DP playing defense (for FLEX or another player → OPO), FLEX batting for DP, lineup dropping to 9. Currently the DP spot is offense-only and FLEX defense-only.
 2. **Pitcher-specific rules** — restrictions on a removed pitcher returning to the circle.
 3. **Courtesy runners** for pitcher/catcher (do not count as substitution).
 4. **Offensive substitutions other than PH/PR** — currently only the upcoming batter or a runner can be replaced while batting.
 5. **Systematic substitution strategy** — new `ISubstitutionStrategy` beside `RandomSubstitutionStrategy`: pull the pitcher after a series of hits/walks (per-game `PlayerStats` of the current pitcher), pull a batter after 2–3 failed at-bats, pick replacements by attributes instead of at random.
 6. **Per-team strategy** — `MatchSimulator` uses one strategy for both teams; allow a different one per team (e.g. user-chosen coaching style for auto-simulated games).

## Match simulation

 1. **One simulator per match** — `ScheduleRowTile` creates its own `MatchSimulator` (on every refresh), independent from the one in `MatchTab`, so game state (inning, outs, runners) diverges between them.
 2. **Simulator state is not persisted** — after loading a saved game, an `ACTIVE` match restarts from inning 1 with empty bases.
 3. **Match lineup is the team's default lineup** — `League.scheduleMatches` passes `team.getDefaultLineup()` into every `Match`, so all matches of a team share one `Lineup` instance. `Lineup.setUp` then writes the match stats onto the default lineup's `PlayerRecord`s and resets the starters' stats on each new match; finished matches lose their own lineup and box score; in-game substitutions stay visible in `LineupTab` until the next game's `setUp`; `Team.setLineup` leaves scheduled matches holding the old lineup object. Consider a per-match lineup copy.
 4. **`Team.randomizeLineup` can put the DP into spot 10** — positions are drawn at random for all batting spots incl. the FLEX spot, so the DP may land in spot 10 and never bat. DP must be in spots 1–9 and spot 10 must hold a defensive position (the FLEX).
 5. **`Team.randomizeLineup` crashes on small rosters** — the substitutes loop calls `availablePlayers.remove(rand.nextInt(size))` without checking the list is non-empty, so a roster too small to fill the bench throws `IllegalArgumentException`. The loop condition (`i <= SUBSTITUTES || i < availablePlayers.size()`) is also off and keeps drawing past spot 8.
 6. **`LineupTile.checkLineup` misses bench duplicates** — it only checks position players against each other; a starter also listed as a substitute, or one player listed twice on the bench, is accepted. Harmless in-game (`Lineup.getAvailableReplacements` filters them) but the lineup is wrong.

## GUI

 1. **Migrate to AtlantaFX 3.0.0** — released 2026-09-24; the project is on 2.1.0. Check the changelog for breaking changes (theme classes, `Styles` constants, looked-up `-color-*` vars), bump in root `pom.xml`, run `GuiSmokeTest`. **ARCHITECTURE.md:** Tech Stack (UI row), Cross-cutting patterns (GUI kit) if theme names change; also the theme bullet in `gui.instructions.md`.
 2. **`ProgressIndicatorUtil` is unused** — third-party gist with inline black/white styles; delete it or restyle on theme vars if a dimming overlay is wanted (the live spinner is in `Softman.setupStage`).
 3. **Theme preference** — the dark-theme choice is not persisted and the app always starts light; optionally seed from `Platform.getPreferences().getColorScheme()`. **ARCHITECTURE.md:** Cross-cutting patterns (GUI kit); also the "`NordLight` by default" theme bullet in `gui.instructions.md`.
 4. **Dev tools (GUI plan 3.3, optional)** — dev-only DevToolsFX or Scenic View for live scene-graph inspection. **ARCHITECTURE.md:** Tech Stack (UI and Run plugin rows), Quick Commands if it adds a launch profile.

## Documentation

 1. `docs/README.md` is not linked from `ARCHITECTURE.md` or `README.md` (unreachable documentation file), and its references still point to `.creed/SUGGESTIONS.md` instead of `.creed/BACKLOG.md` for pending work, known bugs and the backlog. Link it from `ARCHITECTURE.md`/`README.md` (and update its links) or remove it.
