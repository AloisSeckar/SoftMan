# SoftMan — Agent Guide

Java 25 / JavaFX desktop softball manager game. Maven multi-module.

## Modules

| Module | Contains | Rules |
| --- | --- | --- |
| `softman-core` | Game logic (`elrh.softman.logic`, `elrh.softman.utils`) | **Must not import JavaFX or JDBC.** Deps: slf4j, commons-lang3, Lombok only |
| `softman-db` | ORMLite + SQLite persistence (`elrh.softman.db`) | Depends on core |
| `softman-desktop` | JavaFX client (`elrh.softman.gui`, main class `elrh.softman.Softman`) | Depends on core + db |

## Commands

```bash
mvn clean install                          # full build from root
mvn -pl softman-desktop -am compile        # fast desktop compile check
mvn -pl softman-desktop -am test           # tests (incl. TestFX smoke test)
mvn -pl softman-desktop -am javafx:run     # run the app
```

A `debug` execution of `javafx-maven-plugin` exists (JDWP on port 8000).
Working directory for run/test is the repo root — `softman.db`, `sav/` and `log/` resolve there.

## Conventions

- Java 25 source/target; modern language features (records, sealed, pattern matching) welcome
- Lombok everywhere, incl. `@Slf4j` — log via the generated `LOG` field (wildcard `import lombok.*` is not possible, all must be explicit)
- Errors: `ErrorUtils.handleException(source, ex)` / `ErrorUtils.raise(msg)`; operations return `Result`
- Singletons use the `getInstance()` pattern — keep it, do not introduce DI
- Logging: SLF4J API + `slf4j-simple` (`simplelogger.properties` per module)
- Tests: JUnit 6 + Hamcrest

## Off-limits

Never edit or read for context: `target/`, `sav/`, `log/`, generated sources, `*.db`.

## Comment policy

One short line, only when the code cannot show it itself.
Comment why, not what.
No essays, no doc-comment boilerplate, no change-log or "what I changed" comments.

## GUI work

**Read `docs/archive/GUI.md` before touching anything under `softman-desktop`.** It holds the locked-in technology decisions and the (completed) phased modernization plan.

Summary of the locked decisions:

- JavaFX, plain Java — **no FXML, no Scene Builder**, screens are data-driven
- Theme: AtlantaFX `NordLight` (light!) by default, `NordDark` via Show → Dark theme; CSS must read on both; accent Nord Frost `#5E81AC`
- Build UI through the `elrh.softman.gui.kit` package (`Cards`, `Tables`, `Ratings`, `Icons`, `Images`, `Layouts`, `Tokens`) — never ad-hoc
- No hex literals in Java or CSS; use AtlantaFX looked-up vars and `-sm-*` tokens
- No `setLayoutX/Y`, no `AnchorPane` pixel anchors, no fixed px sizes
- Rewrite the view layer only — public methods (`setMatch`, `reload`, `refresh`, listener registration) stay
- UI libraries: AtlantaFX, Ikonli (Feather), ControlsFX. BootstrapFX, Medusa and FontAwesomeFX are gone — do not reintroduce

## Verification split

The agent runs the build and tests, then **stops**. The human runs the app and judges the visuals.
Never speculate about how a change looks on screen.

## Other docs

`docs/BACKLOG.md` — deferred tasks and known issues.
`docs/SUMMARY.md` — snapshot map of the codebase.

**Keep `docs/SUMMARY.md` current:** when a change makes any of its statements stale (file or test counts,
packages, dependencies, game flow, feature status, known bugs), update it in the same change and bump the
"refreshed" date in its header.
