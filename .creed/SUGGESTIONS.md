# AgentCreed Suggestions

<!-- ↓↓↓ agentcreed ↓↓↓ -->

This file is used to collect Agentic AI suggestions discovered during development.

Once the suggestion is resolved, it should be removed from here.

Because the suggestions may vary based on the personalised way of interacting with AI, it is located inside gitignored `.creed` folder and is not meant to be versioned.

<!-- ↑↑↑ agentcreed ↑↑↑ -->

## Rule conflicts

Following points are noted. Will be solved in next AgentCreed version and delivered here via an update script. No need to report again or fixing in this repository.

- `creed-dev` says "leave the verification up to the user"; `AGENTS.md` says the agent runs build and tests, then stops. Proposed: project rule (`AGENTS.md`) wins — agent builds/tests, never runs the app.
- `creed-dev` asks for doc comments on all exported constants/functions; `AGENTS.md` comment policy forbids doc-comment boilerplate. Proposed: `AGENTS.md` wins.
- `SECURITY.md` is empty — no project-specific boundaries defined yet.

## Possible improvements

- Remove unused `javafx-fxml` dependency from `softman-desktop` (no `javafx.fxml` imports; FXML is banned).
- Move JDBC URLs (`SOURCES_DB`, `GAME_DB`) from core `Constants` to `softman-db`; `GAME_DB` hardcodes a Windows `\` separator.
- Update `README.md` module table (three modules; persistence is in `softman-db`).
