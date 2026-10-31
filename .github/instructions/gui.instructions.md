---
applyTo: "softman-desktop/**"
---

# GUI rules (background and history: `docs/archive/2026-javafx-gui-update.md`)

- JavaFX in plain Java (no Kotlin, Compose or web frontend), data-driven screens — no FXML, no Scene Builder
- Theme is AtlantaFX `NordLight` by default with a runtime `NordDark` toggle (Show → Dark theme) — every rule must read on both; never hardcode surface or text colours; accent is Nord Frost (`-color-accent-5`); match results use `-sm-color-win` / `-sm-color-loss` / `-sm-color-draw`
- Build UI through `elrh.softman.gui.kit` (`Cards`, `Tables`, `Ratings`, `Icons`, `Images`, `Layouts`, `Tokens`) — never ad-hoc
- Reference implementation: `ClubInfoTile` (a tile is a `Card`; header = identity, body = content). Row items inside a card are plain rows, not cards
- Navigation: `ContentFrame.getInstance().switchTo(Screen.X)`; new tabs get a `Screen` constant
- No hex literals in Java or CSS; raw colours live only in the `.root` token block of `softman.css`. Elsewhere use AtlantaFX looked-up vars (`-color-*`) and `-sm-*` tokens
- New style classes: define in `softman.css` with the `sm-` prefix and add a constant to `Tokens`
- Spacing from `Layouts.Space` (4/8/12/16/24); sizes via `Layouts.em(...)`
- No `setLayoutX/Y`, no `AnchorPane` pixel anchors, no fixed px sizes
- UI libraries: AtlantaFX, Ikonli (Feather pack) for icons, ControlsFX; add others only if well-maintained OSS (MaterialFX and JFoenix are dead). BootstrapFX, Medusa and FontAwesomeFX are removed — do not add them back
- When restyling, change view construction only; keep public methods (`setMatch`, `reload`, `refresh`, listener registration) and logic untouched
- Verify with `mvn -q -pl softman-desktop -am test` (TestFX smoke test), then stop — see Verification split in `AGENTS.md`
