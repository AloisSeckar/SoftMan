---
applyTo: "softman-desktop/**"
---

# GUI rules (full plan in `GUI.md`)

- Theme is AtlantaFX `NordLight` — **light** surfaces; never assume a dark background
- Build UI through `elrh.softman.gui.kit` (`Cards`, `Tables`, `Ratings`, `Icons`, `Layouts`) — never ad-hoc
- No hex literals in Java or CSS; only AtlantaFX looked-up vars (`-color-*`) and `-sm-*` tokens
- No `setLayoutX/Y`, no `AnchorPane` pixel anchors, no fixed px sizes
- Icons via Ikonli (Feather pack), not FontAwesomeFX; no new BootstrapFX or Medusa usage
- Rewrite view construction only; keep public methods (`setMatch`, `reload`, `refresh`, listener registration) and logic untouched
- Verify with `mvn -q -pl softman-desktop -am test` (TestFX smoke test), then stop — the human judges visuals
