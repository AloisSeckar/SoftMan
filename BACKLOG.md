# SoftMan — Backlog

Tasks deferred for later. Remove an entry once it is done.

## Match substitutions

- [ ] **DP/FLEX rules** — DP playing defense (for FLEX or another player → OPO), FLEX batting for DP,
  lineup dropping to 9. Currently the DP spot is offense-only and FLEX defense-only.
- [ ] **Pitcher-specific rules** — restrictions on a removed pitcher returning to the circle.
- [ ] **Courtesy runners** for pitcher/catcher (do not count as substitution).
- [ ] **Offensive substitutions other than PH/PR** — currently only the upcoming batter or a runner
  can be replaced while batting.

## Match simulation

- [ ] **One simulator per match** — `ScheduleRowTile` creates its own `MatchSimulator` (on every refresh),
  independent from the one in `MatchTab`, so game state (inning, outs, runners) diverges between them.
- [ ] **Simulator state is not persisted** — after loading a saved game, an `ACTIVE` match restarts
  from inning 1 with empty bases.
- [ ] **Match lineup is the team's default lineup** — `League` passes `team.getDefaultLineup()` into
  `Match`, so all matches of a team share one `Lineup` instance. In-game substitutions stay visible in
  `LineupTab` after the game until the next game's `setUp`, and past matches don't keep their own lineups.
- [ ] **Starter stats written into default lineup records** — `Lineup.setUp` sets the match stats on the
  default lineup's `PlayerRecord`s (consequence of the shared lineup above).
