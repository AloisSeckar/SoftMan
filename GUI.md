# SoftMan GUI — Technology Decisions & Modernization Plan

## Decisions (locked)

| Question | Decision |
| --- | --- |
| Frontend toolkit | **JavaFX**, Java (no Kotlin, no Compose Multiplatform, no web) |
| FXML / Scene Builder | **No.** Screens are data-driven; FXML has no leverage and does not affect looks |
| Theme | **AtlantaFX `NordLight`** (`NordDark` is the free pair for a later toggle) |
| Brand colour | Green identity **dropped**. Accent = Nord Frost `#5E81AC`. Aurora green `#A3BE8C` / red `#BF616A` reserved for win/loss semantics |
| JavaFX version | Bump **21.0.5 → 25 LTS** up front; deal with fallout if it appears |
| Third-party libs | Well-maintained OSS allowed |
| Scope | Rewrite the **view layer**; wiring, logic and public APIs stay |
| Singletons | Keep `getInstance()` everywhere — least churn, wiring untouched |
| Java level | Modern Java 25 features welcome (records for view models, sealed, pattern matching) |
| Lombok | Yes, in view/kit code, same as the rest of the project |
| Kit package | `elrh.softman.gui.kit` |
| Verification split | Agent runs `mvn -q -pl softman-desktop -am compile` + TestFX smoke test, then stops. Human runs the app and eyeballs. Agent must not speculate about visual results |

### Why not FXML

FXML pays off for static forms, many similar dialogs, and designer/developer separation. It costs a
second language, reflection-based `@FXML` injection (runtime failures instead of compile errors), and
refactors that don't propagate.

SoftMan's screens are data-driven, not static — standings tables, lineup rows, box-score grids,
schedule rows, 16 attribute widgets, play-by-play. All generated in loops from model objects. FXML has
no leverage there, and it does nothing for appearance; that is CSS's job.

### Library verdicts (surveyed Aug 2026)

| Library | Status | Verdict |
| --- | --- | --- |
| AtlantaFX 2.1.0 | MIT, actively developed | **Adopt** — CSS-first theme + `Card`/`Tile`/`Message`/`ModalPane`/`Notification`/`TabLine` |
| ControlsFX 11.2.4 | Very active | **Keep and use more** (Notifications, SegmentedButton, MasterDetailPane, GridView) |
| Ikonli 12.4.0 | Stable | **Adopt**, replaces FontAwesomeFX |
| BootstrapFX 0.4.0 | Stale, Bootstrap-3 look | **Remove** — part of the dated look |
| Medusa 16.0.0 | Maintained but skeuomorphic dials | **Remove** — flat rating bars read better |
| FontAwesomeFX | Declared, unused | **Remove** |
| MaterialFX | Dead (~2023) | Avoid |
| JFoenix | Dead | Avoid |
| GemsFX / TilesFX / CalendarFX / JMetro | Alive | Optional / later |
| FXGL, Scene Builder 26 | Alive | Not applicable here |

There is no "React for JavaFX". The mature 2026 pattern is **a good theme plus a thin in-house
component kit** — which is what this plan builds.

## Consequences of a light theme

The existing stylesheet assumes dark surfaces. NordLight requires inversion, not retinting:

- `.table-view { -fx-background-color: #1d1d1d }` — delete entirely
- `.output-window` (dark Courier `TextArea` in MatchTab) — light surface, dark text
- `.odd-row` / `.even-row` (lightgreen / mediumseagreen) — delete, use AtlantaFX `.striped`
- `.menu-frame` `#228B22` / `.menu-bar` `#9ACD32` — neutral Nord Snow Storm surfaces
- `.action-button` red 30×30 — themed accent/danger button
- **Asset audit required**: `img/vecteezy/field.png`, `img/teams/*`, `img/faces/*` may assume a dark
  backdrop. Re-export or place on a card surface.

## Plan

### Phase -1 — Groundwork for AI-assisted work (do FIRST)

Purpose: stop every session re-deriving the same facts, and cap token burn. The repo currently has
**no** `AGENTS.md`, **no** `.github/copilot-instructions.md`, **no** `*.instructions.md`, **no**
`.editorconfig`. Only `.gitignore` exists (it already covers `target/`, `.vscode/`, `sav/`, `log/`).

**-1a. Root `AGENTS.md`** — max ~60 lines, terse; long files get skimmed and cost tokens every turn.

- Module map: `softman-core` (game logic, must not import JavaFX), `softman-db` (ORMLite + SQLite),
     `softman-desktop` (JavaFX)
- Commands: `mvn clean install`; `mvn -pl softman-desktop -am javafx:run`;
     mainClass `elrh.softman.Softman`; a debug profile already exists in `javafx-maven-plugin`
- Conventions: Java 25 source/target, Lombok, SLF4J (+simple), `getInstance()` singleton pattern
- Off-limits: `target/`, `sav/`, `log/`, generated sources
- Comment policy: one short line, only when the code cannot show it. No essays, no change-log comments
- Pointer: "read `GUI.md` before any GUI work"

**-1b. `.github/instructions/gui.instructions.md`** with frontmatter `applyTo: "softman-desktop/**"`
   so these apply automatically without restating them each session:

- Build UI through `elrh.softman.gui.kit` (`Cards`, `Tables`, `Ratings`, `Icons`, `Layouts`) — never ad-hoc
- No hex literals in Java or CSS; only AtlantaFX looked-up vars and `-sm-*` tokens
- No `setLayoutX/Y`, no `AnchorPane` pixel anchors, no fixed px sizes
- Preserve existing public methods (`setMatch`, `reload`, `refresh`, listener registration) —
     view construction only, no logic changes
- Theme is `NordLight` — **light**; never assume dark surfaces
- Once the golden template exists: "match `ClubInfoTile` as the reference implementation"

**-1c. `.vscode/settings.json`** — `files.exclude` + `search.exclude` for `**/target`, `log/`, `sav/`.
   The workspace overview currently lists every `target/classes/**` tree, which is pure waste on every
   prompt. `.vscode/` is gitignored, so this stays local.

**-1d. Record the locked conventions** (table above) in `AGENTS.md` so no session has to guess.

**-1e. TestFX headless smoke test** in `softman-desktop/src/test/java`:

- Deps: `org.testfx:testfx-core` + `testfx-junit5` (plus Monocle if a headless environment is needed)
- Instantiates `MainLayout` and asserts all 9 tabs and all 10 tiles construct without exception
- This is the unattended safety net — it turns "did I break it?" into an exit code and catches
     NPEs and missing-CSS-class breakage the agent cannot see
- **Must pass before Phase 0 starts**, so it is a true regression baseline

**-1f. Convert the phase list below into a checkbox checklist** for cross-session progress tracking.

**-1g. Working discipline** (process, not code):

- Branch per phase; commit after each verified step — `git reset` is free, agent undo is not
- **One phase per session.** Open each session with "read `GUI.md` + `AGENTS.md`, execute Phase X step N"
- Phase 1 plus exactly one tile (`ClubInfoTile`), human-reviewed, becomes the **golden template**;
     every later tile references it instead of open-ended design. Single biggest token saver

### Phase 0 — Foundation (blocking)

1. Root `pom.xml`: `fx.version` 21.0.5 → 25.x LTS. Rebuild and smoke-test before touching UI.
2. Add to root `dependencyManagement` and `softman-desktop/pom.xml`:
   `io.github.mkpaz:atlantafx-base:2.1.0`, `org.kordamp.ikonli:ikonli-javafx:12.4.0`,
   `org.kordamp.ikonli:ikonli-feather-pack:12.4.0`; bump ControlsFX to 11.2.4.
3. `Softman.java`: `Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet())`
   before building `MainLayout`; keep `softman.css` as an additional Scene stylesheet.
4. Run and screenshot the baseline. Expect visual breakage from dark-assuming CSS.

### Phase 1 — Design system (blocking for Phase 2)

1. Rewrite `softman-desktop/src/main/resources/css/softman.css` as a **token layer over NordLight**:
   - Delete every hardcoded hex; reference `-color-accent-emphasis`, `-color-bg-default`,
     `-color-bg-subtle`, `-color-fg-default`, `-color-fg-muted`, `-color-border-default`,
     `-color-success-*`, `-color-danger-*`
   - Override the accent to Nord Frost `#5E81AC`
   - Add game-semantic tokens: `-sm-color-win` `#A3BE8C`, `-sm-color-loss` `#BF616A`,
     `-sm-color-draw` `#EBCB8B`, `-sm-color-home`, `-sm-color-away`
   - Define a spacing scale (4/8/12/16/24) and a type scale (caption/body/title/display) as classes
   - Remove `.padding-5`, `.font-courier-12`, `.player-name{30px}`, `.framed`, `.info-full`,
     `.info-brief`, `.odd-row`, `.even-row`, and the dark `.table-view` block
2. New package `elrh/softman/gui/kit/`:
   - `Cards` — factory over AtlantaFX `Card`; replaces BootstrapFX `Panel` and the `.info-*` boxes
   - `Tables` — fluent `TableView`/`TableColumn` builder (width, style class, value factory,
     sortable, numeric alignment)
   - `Ratings` — flat 0–100 attribute bar; replaces `GUIUtils.getGauge()`
   - `Icons` — Ikonli wrapper with uniform sizing and colour
   - `Layouts` — `row` / `column` / `spacer` / `grow` helpers

### Phase 2 — Screen rewrites

Parallelizable after Phase 1. Rewrite view construction only; keep `setMatch`, `reload`, `refresh`
and listener registrations so wiring is untouched.

1. **Chrome** — `MenuFrame` (green bar → neutral toolbar + icons), `FocusFrame`, `ActionFrame`
   (red 30×30 → themed buttons), `ContentFrame` (styled tabs + enum-keyed navigation replacing the
   hardcoded `switchTo(String)` switch)
2. **Tiles** — all 10 classes in `gui/tile/` re-based on `Cards`. *This is the fix for cross-tab inconsistency.*
3. **PlayerAttributesTile** — 16 Medusa gauges → `Ratings` bars in 4 groups *(parallel with 8)*
4. **Tables** — `LeagueStadingsTable` and `TeamPlayersTable` via `Tables`; apply `.striped` /
    `.bordered` / `.dense` *(parallel with 8)*
5. **DefenseTile** — bind label `layoutX/Y` to fractions of pane width/height; verify `field.png` on light bg
6. **LineupTab / TrainingTab / StandingsTab** — AnchorPane + hardcoded anchors
    (`setTopAnchor(saveButton, 585d)`) → resizable `BorderPane` / `GridPane`
7. **PlayerTab** — `StringUtils.leftPad/rightPad` monospace block → real `TableView` via `Tables`
8. **MatchTab** — play-by-play on a light monospace surface; simulate/play buttons in a button group
9. **Asset contrast pass** — `img/teams/*`, `img/faces/*`, `img/vecteezy/field.png` against NordLight

### Phase 3 — Cleanup

 1. Remove `bootstrapfx-core`, `fontawesomefx-commons`, `fontawesomefx-fontawesome`, `Medusa` from both poms
 2. Light/dark toggle in `MenuFrame` — swap `NordLight` ⇄ `NordDark` at runtime; optionally seed from
    `Platform.getPreferences().getColorScheme()` (available after the JavaFX 25 bump)
 3. Optional dev-only DevToolsFX / Scenic View for live scene-graph inspection

## Out of scope

FXML/Scene Builder; changes to `softman-core` or `softman-db`; the JavaFX-types-in-core coupling
documented in `PLAN.md`; new features (Save/Load, Training, Market, Stats); navigation architecture
beyond the enum swap.

## Verification

1. `mvn -pl softman-desktop -am test` — TestFX smoke test green (baseline from Phase -1)
2. `mvn clean install` at root — compiles on Java 25 / JavaFX 25
3. `mvn -pl softman-desktop -am javafx:run` — all 9 tabs open, no exceptions
4. Per Phase-2 item: open the tab, resize the window, confirm no clipping (esp. LineupTab,
   StandingsTab, DefenseTile)
5. Contrast check: no light-grey-on-white text; play-by-play and all tables readable
6. "Next day" and simulate a match — `SimulationService` `Platform.runLater` refreshes repaint correctly
7. ScheduleRow S/P/V buttons and PlayerTab match hyperlink — navigation survives the enum refactor
8. Grep the gui package for `bootstrapfx`, `de.jensd`, `eu.hansolo`, `#[0-9a-fA-F]{6}` → zero before
   closing Phase 3
9. Before/after screenshots of ClubTab, MatchTab, TeamTab, PlayerTab
