# AgentCreed Suggestions

<!-- ↓↓↓ agentcreed ↓↓↓ -->

This file is used to collect Agentic AI suggestions discovered during development.

This file holds pending suggestions only. Resolved items are deleted, never marked as done.

<!-- ↑↑↑ agentcreed ↑↑↑ -->

## Rule conflicts

Following points are noted. Will be solved in next AgentCreed version and delivered here via an update script. No need to report again or fixing in this repository.

- `SECURITY.md` is empty — no project-specific boundaries defined yet.

## TODO LIST

- Unexplained stats bug, observed once (2026-10-31): after a simulated match with a substitution (starter replaced by a bench player in the same spot), the saved and reloaded game credited both stats lines of that match to the starter; the substitute had none. The play-by-play stats printout was correct. Not reproducible: tests of a single match, 30 full simulated games, and a save/load round trip all credited stats correctly. The user will start a new simulation and watch whether it recurs. If it does, capture the save before and after loading, and check whether the starter's `PlayerStats` rows carry the substitute's `playerId`.
- `League.scheduleMatches` gives every match the team's default `Lineup` object, so all matches of a team share one lineup. `Lineup.setUp` then resets the starters' stats on each new match, and finished matches lose their own lineup and box score. `Team.setLineup` also leaves scheduled matches holding the old lineup object. Consider a per-match lineup copy.
- New game (Game menu) leaves the world half reset: `AssociationManager.reset()` calls `user.reset()`, which fires `focusedTeamChanged(null)`; `PlayerTab` and `LineupTab` dereference the team without a null check, so an NPE aborts `reset()` before the maps are cleared and the handler never reaches `populateAssociation()` and `setUp()`. Load game from the menu goes through the same `reset()` and presumably fails the same way. Fix: null-guard both listeners (`PlayerTab.focusedTeamChanged`, `LineupTab.focusedTeamChanged`).
- `AssociationFactory` keeps the 16 clubs as `static` constants, so a second New game in one session reuses clubs that still hold the previous game's teams, money and registration year. New teams become "B" and "C", a third New game throws "Max 3 teams allowed for each level", the focused team is the old team, and `LineupTab.saveLineup` (`getActiveClub().getTeams().get(0)`) edits the old team. Fix: create fresh `Club` objects per game, or clear each club's teams and reset its state in `AssociationManager.reset()`.
- Starting or loading a game has no single routine (existing `TODO unify actions performed upon starting new game` in `Softman.setupStage` and `MainLayout.setUp`). After New game or Load game, the "Current date" label (`ActionFrame`), the `FocusFrame` club and team selectors, the `ClubTab` league table and the schedule are not refreshed. Fix: one routine used by startup, New game and Load game.
- `Softman.setupStage` calls `AssociationManager.nextDay()` at startup. This simulates the first day (after a confirmation prompt) and is also the only thing that initializes the "Current date" label. Decide whether startup should keep simulating day 1 or just set the date label.
