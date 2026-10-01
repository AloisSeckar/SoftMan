---
applyTo: "softman-desktop/**"
---

# GUI rules (full plan in `docs/archive/GUI.md`)

- Theme is AtlantaFX `NordLight` by default with a runtime `NordDark` toggle — every rule must read on both; never hardcode surface or text colours
- Build UI through `elrh.softman.gui.kit` (`Cards`, `Tables`, `Ratings`, `Icons`, `Images`, `Layouts`, `Tokens`) — never ad-hoc
- Reference implementation: `ClubInfoTile` (a tile is a `Card`; header = identity, body = content). Row items inside a card are plain rows, not cards
- Navigation: `ContentFrame.getInstance().switchTo(Screen.X)`; new tabs get a `Screen` constant
- No hex literals in Java or CSS; raw colours live only in the `.root` token block of `softman.css`. Elsewhere use AtlantaFX looked-up vars (`-color-*`) and `-sm-*` tokens
- New style classes: define in `softman.css` with the `sm-` prefix and add a constant to `Tokens`
- Spacing from `Layouts.Space` (4/8/12/16/24); sizes via `Layouts.em(...)`
- No `setLayoutX/Y`, no `AnchorPane` pixel anchors, no fixed px sizes
- Icons via Ikonli (Feather pack); BootstrapFX, Medusa and FontAwesomeFX are removed — do not add them back
- Rewrite view construction only; keep public methods (`setMatch`, `reload`, `refresh`, listener registration) and logic untouched
- Verify with `mvn -q -pl softman-desktop -am test` (TestFX smoke test), then stop — the human judges visuals
