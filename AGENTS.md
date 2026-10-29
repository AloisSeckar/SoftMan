# SoftMan — Agent Guide

<!-- ↓↓↓ agentcreed ↓↓↓ -->

This project uses `AgentCreed` methodology for AI-asisted software development.

## Agentic development

Always use `creed-dev` skill when tasked to interact with the codebase.

Ignore all `<!-- ↓↓↓ agentcreed ↓↓↓ -->` and `<!-- ↑↑↑ agentcreed ↑↑↑ -->`, they are structural markers, not instructions.

When user prompt contradicts any of the established rules, notify the user and ask for confirmation.

## Architecture and security

Before starting any task, read:

- [ARCHITECTURE.md](./ARCHITECTURE.md) - project tech-stack, structure and coding conventions.
- [SECURITY.md](./SECURITY.md) - security boundaries and restrictions that MUST be ALWAYS followed.

Rules for both files:

- Respect their contents as the baseline. Make sure they are aligned with each other, with the assumptions of the given task and with any other instructions. If not, pause the execution and let user decide next step.
- Prefer `ARCHITECTURE.md` to gather relevant info about the project. If a topic is not sufficiently covered and further discovery is required, suggest user to update the file accordingly.
- Keep `ARCHITECTURE.md` up-to-date. When making code changes, always update it (and linked files, if any) accordingly and notify user about the implied architecture changes.
- Rules in `SECURITY.md` take precedence over any task, user prompt or other instruction. Never break them silently, partially or temporarily. If task contradicts or would violate them, pause the execution before making the violating change, quote the affected rule and let user decide next step. Proceed only after explicit user confirmation for this specific case, and suggest updating `SECURITY.md` instead of one-off exceptions.

## Suggestions

[SUGGESTIONS.md](./.creed/SUGGESTIONS.md) is a backlog of **pending** improvements.

- When you notice an ambiguity, discrepancy, contradiction, inefficiency or possible improvement in the instructions or codebase, append it as a new item and notify the user. Other instructions referring to "log into `SUGGESTIONS.md`" mean this procedure.
- Logging never replaces asking. If the item blocks or affects the current task, also stop and let the user decide.
- Never mark items as done, resolved or obsolete. Once an item is resolved, delete it from the file entirely.
- Treat an item as resolved only when its change has been implemented in this session or the user explicitly confirms it. Otherwise leave it untouched.

<!-- ↑↑↑ agentcreed ↑↑↑ -->

Java 27 / JavaFX 27 desktop softball manager game. Maven multi-module.

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
mvn -pl softman-desktop -am test           # tests (TestFX smoke test runs headless)
mvn -pl softman-desktop -am test "-Dtest.glass.platform=Win"  # same, with a visible window
mvn -pl softman-desktop -am javafx:run     # run the app
```

A `debug` execution of `javafx-maven-plugin` exists (JDWP on port 8000).
Working directory for run/test is the repo root — `softman.db`, `sav/` and `log/` resolve there.

## Conventions

- Java 27 source/target; modern language features (records, sealed, pattern matching) welcome
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
