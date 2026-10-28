# ARCHITECTURE.md

<!-- ↓↓↓ agentcreed ↓↓↓ -->

This file describes project tech-stack, structure and coding conventions. Rules for handling this file are defined in [AGENTS.md](./AGENTS.md).

<!-- ↑↑↑ agentcreed ↑↑↑ -->

## Overview

SoftMan is a single-player desktop softball club/team management simulator. It is a Maven multi-module monolith:
a framework-free game core, an SQLite persistence adapter and a JavaFX client.

Detailed feature status, data model, game flow and known bugs live in [docs/SUMMARY.md](./docs/SUMMARY.md);
deferred work in [docs/BACKLOG.md](./docs/BACKLOG.md). This file holds only the structural baseline.

## Tech Stack

| Category | Technology |
| --- | --- |
| Language | Java 27 (`maven.compiler.release` 27), OpenJDK |
| Build | Maven multi-module (`elrh:softman:1.0-SNAPSHOT`), compiler plugin 3.15.0, surefire 3.5.4 |
| UI | JavaFX 27 (programmatic, no FXML), AtlantaFX 2.1.0 (`NordLight` / `NordDark`), Ikonli 12.4.0 (Feather), ControlsFX 11.2.4 |
| Persistence | SQLite (`sqlite-jdbc` 3.51.1.0) via ORMLite 6.1, schema built by hand in `TableConfigs` |
| Utilities | Lombok 1.18.48 (`LOG` field via `lombok.config`), commons-lang3 3.20.0 |
| Logging | SLF4J 2.0.17 API + `slf4j-simple` (`simplelogger.properties`, output to `log/softman.log`) |
| Testing | JUnit 6.0.2 (Jupiter), Hamcrest 3.0, TestFX 4.0.18 (headless glass platform by default) |
| Run plugin | `javafx-maven-plugin` 0.0.8 (main class `elrh.softman.Softman`, extra `debug` execution with JDWP on port 8000) |
| CI / infra | None — no CI workflows, no containers; local desktop app only |

## Architecture

Ports & adapters around a singleton façade. Dependencies point inward to `softman-core`.

```text
softman-desktop  (JavaFX client, entry point, wiring)
   │        │
   │        ▼
   │   softman-db  (SqliteGameRepository, SqliteNameSource, OrmLite DAOs)
   │        │
   ▼        ▼
softman-core  (domain, simulation, managers, ports: IGameRepository, INameSource, ISimulationRunner, ...)
```

| Module | Packages | Allowed dependencies |
| --- | --- | --- |
| `softman-core` | `elrh.softman.logic`, `elrh.softman.utils` | commons-lang3, Lombok, SLF4J only — **no JavaFX, no JDBC** |
| `softman-db` | `elrh.softman.db` | core + sqlite-jdbc + ORMLite |
| `softman-desktop` | `elrh.softman` (`Softman`), `elrh.softman.gui.*` | core + db + JavaFX and UI libraries |

Key traits:

- **Façade** — `AssociationManager` (singleton) owns the world: clubs, leagues, matches, clock (`ClockManager`), user focus (`UserManager`).
- **Ports** — core defines interfaces in `logic.interfaces`; desktop/db supply implementations, wired in `Softman.setupGame()` / `setupStage()`.
- **Snapshot persistence** — whole world is saved/loaded in one transaction by `SqliteGameRepository`; no active record, nothing hits the DB during simulation. Guarded by `GameMeta.SCHEMA_VERSION`.
- **Two databases** — read-only name pool `softman.db` (seeded from `names.sql`) and save file `sav/game-<id>.db` (single slot `career`).
- **Simulation** — `MatchSimulator` + `SimUtils` / `StatsUtils` in core; desktop runs batches through `SimulationController` → `SimulationService` (JavaFX `Service` + `ForkJoinPool.commonPool()`).
- **Observer** — `UserManager` notifies `IFocusedClubListener` / `IFocusedTeamListener` (GUI tabs) about focus changes.
- **Identity** — every entity keyed by `UUID`; managers use `LinkedHashMap<UUID, …>` for stable order.
- **Domain / data split** — rich domain classes in `logic.core` wrap Lombok data holders in `logic.core.data` (extending `AbstractEntity`).

## Key Directories

```text
SoftMan/
├── pom.xml                      # parent POM, dependencyManagement, plugin config
├── lombok.config                # log field renamed to LOG
├── names.sql                    # seed data for softman.db name pool
├── softman.db                   # read-only name-pool DB (do not edit/read)
├── softman-core/src/main/java/elrh/softman/
│   ├── logic/                   # AssociationManager, MatchSimulator, Result record
│   │   ├── core/                # League, Club, Team, Player, Match, Lineup
│   │   │   ├── data/            # AbstractEntity + plain data holders (*Info, PlayerStats, ...)
│   │   │   └── stats/           # Standing, BoxScore
│   │   ├── managers/            # ClockManager, UserManager
│   │   ├── enums/               # PlayerPosition, PlayerLevel, MatchStatus, ...
│   │   ├── interfaces/          # ports + listener contracts (I* prefix)
│   │   └── strategy/            # RandomSubstitutionStrategy
│   └── utils/                   # Constants, ErrorUtils, SimUtils, StatsUtils, Utils
│       └── factory/             # Association/Club/Team/PlayerFactory (world generation)
├── softman-core/src/test/       # pure in-memory JUnit tests (elrh.softman.test)
├── softman-db/src/main/java/elrh/softman/db/
│                                # GameDatabase, TableConfigs, SqliteGameRepository,
│                                # SqliteNameSource, LocalDatePersister, persistence-only rows
├── softman-desktop/src/main/java/elrh/softman/
│   ├── Softman.java             # JavaFX Application, setup/teardown, theme switch
│   └── gui/
│       ├── MainLayout.java      # BorderPane root
│       ├── frame/               # MenuFrame, FocusFrame, ContentFrame (+ Screen enum), ActionFrame
│       ├── kit/                 # Cards, Tables, Ratings, Icons, Images, Layouts, Tokens — sanctioned UI builders
│       ├── tab/                 # one class per screen (Club, Match, Team, Player, Lineup, Training, Standings)
│       ├── tile/                # Card-based building blocks of tabs
│       ├── table/               # TableView wrappers
│       ├── sim/                 # SimulationController, SimulationService
│       └── utils/               # FormatUtils, GUIUtils, InfoUtils, ProgressIndicatorUtil
├── softman-desktop/src/main/resources/
│   ├── css/softman.css          # sm-* token layer over AtlantaFX
│   └── img/                     # faces, team logos, vecteezy art, ball/stadium
├── softman-desktop/src/test/    # GuiSmokeTest (TestFX)
├── docs/                        # SUMMARY, BACKLOG, archive/GUI.md, archive/PLAN.md, screenshots
├── .github/instructions/        # gui.instructions.md (applies to softman-desktop/**)
├── .agents/skills/              # AgentCreed skills
├── sav/, log/                   # runtime output, gitignored (off-limits)
└── */target/                    # build output (off-limits)
```

## Entry Points

- Main: `softman-desktop/src/main/java/elrh/softman/Softman.java` (`Application.start` → `setupGame()` load-or-generate → `setupStage()`; Esc / Game → Exit → `closeIfConfirmed()` autosaves)
- World generation: `elrh.softman.utils.factory.AssociationFactory.populateAssociation()`
- Persistence: `elrh.softman.db.SqliteGameRepository` (save/load), `elrh.softman.db.TableConfigs` (schema)
- Tests: `softman-core/src/test/java/elrh/softman/test/`, `softman-desktop/src/test/java/elrh/softman/gui/GuiSmokeTest.java`; `softman-db` has none

## Conventions

- **Naming** — interfaces prefixed `I` (`IGameRepository`); GUI classes suffixed by role (`*Tab`, `*Tile`, `*Frame`, `*Table`); factories in `utils.factory`; utility classes `*Utils`.
- **Singletons** — managers, frames and tabs expose `getInstance()`; keep the pattern, no DI.
- **Errors** — mutating operations return `Result(ok, message)`; exceptions are converted via `ErrorUtils.handleException(source, ex)`, assertions via `ErrorUtils.raise(msg)`.
- **Lombok** — explicit imports only (never `import lombok.*`, it breaks javac 27 on `var`); log through `@Slf4j`'s `LOG`.
- **Core purity** — no JavaFX/JDBC in `softman-core`; persistence annotations never leak into domain classes (schema lives in `TableConfigs`).
- **GUI** — plain Java, no FXML; build via `gui.kit`; no hex colours outside the `.root` token block of `softman.css`; `sm-` prefixed style classes mirrored in `Tokens`; no pixel layout. Full rules: [.github/instructions/gui.instructions.md](./.github/instructions/gui.instructions.md), [docs/archive/GUI.md](./docs/archive/GUI.md).
- **Comments** — one short "why" line only when the code cannot show it.
- **Tests** — JUnit Jupiter + Hamcrest matchers; GUI covered only by a TestFX smoke test.
- **Working directory** — run/test from repo root (surefire and javafx plugin set `workingDirectory` to `${project.basedir}/..`).
- No linters/formatters, git hooks or CI are configured.

## Quick Commands

| Action | Command |
| --- | --- |
| Full build | `mvn clean install` |
| Fast compile check | `mvn -pl softman-desktop -am compile` |
| Test (headless) | `mvn -pl softman-desktop -am test` |
| Test (visible window) | `mvn -pl softman-desktop -am test "-Dtest.glass.platform=Win"` |
| Run | `mvn -pl softman-desktop -am javafx:run` |
| Debug (JDWP :8000) | `mvn -pl softman-desktop -am javafx:run@debug` |

On Windows PowerShell 5.1, redirect Maven output to a file (`*> out.txt`) instead of piping `2>&1`; JDK warnings on stderr otherwise surface as a failed command.

## Needs Clarification

- [ ] `SECURITY.md` has no project-specific rules yet.
- [ ] `softman-core` `Constants` holds JDBC URLs (`jdbc:sqlite:...`) — strings only, but persistence details live in core. `GAME_DB` uses a Windows `\` path separator.
- [ ] `javafx-fxml` is still a desktop dependency although FXML is banned and no source imports `javafx.fxml` — likely removable.
- [ ] No `module-info.java` in any module; JavaFX runs on the classpath.
- [ ] `README.md` module table lists two modules and attributes persistence to core — outdated.
- [ ] The `javafx:run@debug` invocation is inferred from the plugin's `debug` execution id, not documented elsewhere.
