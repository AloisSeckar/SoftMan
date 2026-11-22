package elrh.softman.logic.core;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.utils.Constants;
import elrh.softman.logic.core.data.LeagueInfo;
import elrh.softman.logic.core.data.MatchInfo;
import elrh.softman.logic.core.stats.Standing;
import elrh.softman.utils.ErrorUtils;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @RequiredArgsConstructor
public class League {

    /** Month and day on which every league season starts. */
    public static final MonthDay LEAGUE_START = MonthDay.of(4, 1);

    @Getter
    @NonNull
    private final LeagueInfo leagueInfo;

    @Getter
    private ArrayList<Team> teams = new ArrayList<>();

    private final Map<UUID, Match> matches = new LinkedHashMap<>();

    private final Map<UUID, Standing> standings = new LinkedHashMap<>();

    public UUID getId() {
        return leagueInfo.getLeagueId();
    }

    public String getName() {
        return leagueInfo.getLeagueName() + " " + leagueInfo.getYear();
    }

    /**
     * Returns the league standings sorted by rank.
     *
     * @return unmodifiable list of standings in descending rank order
     */
    public List<Standing> getStandings() {
        return standings.values().stream().sorted().toList();
    }

    public Result registerTeam(Team team) {
        try {
            teams.add(team);
            standings.put(team.getId(), new Standing(getId(), team.getId(), team.getName()));
            team.getTeamInfo().setLeagueInfo(this.leagueInfo);
            LOG.info("Team {} registered", team.getId());
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("League.registerTeam", ex);
        }
    }

    public Result scheduleMatches() {
        try {
            var matchNumberBase = leagueInfo.getMatchNumber();
            var matchesPerRound = teams.size() / 2;
            var rounds = getTotalRounds();
            var roundDate = LEAGUE_START.atYear(leagueInfo.getYear());

            Collections.shuffle(teams);

            for (int i = 1; i <= rounds; i++) {
                for (int j = 0; j < matchesPerRound; j++) {

                    int matchNumber = matchNumberBase + ((i - 1) * matchesPerRound) + (j + 1);

                    int homeTeamIndex;
                    int awayTeamIndex;
                    if (i % 2 == 0) {
                        homeTeamIndex = j;
                        awayTeamIndex = (teams.size() - 1) - j;
                    } else {
                        homeTeamIndex = (teams.size() - 1) - j;
                        awayTeamIndex = j;
                    }

                    var info = new MatchInfo();
                    info.setMatchNumber(matchNumber);
                    info.setMatchDay(roundDate);
                    info.setLeagueId(getId());
                    info.setLeagueRound(i);
                    // TODO not quite nice way to retrieve...
                    info.setStadium(teams.get(homeTeamIndex).getTeamInfo().getClubInfo().getStadium());

                    var match = new Match(info, teams.get(homeTeamIndex).getDefaultLineup(), teams.get(awayTeamIndex).getDefaultLineup());
                    LOG.info("Match: {} - {} @ {}; {} (rnd {})",
                        info.getMatchNumber(),
                        match.getAwayLineup().getLineupInfo().getTeamName(),
                        match.getHomeLineup().getLineupInfo().getTeamName(),
                        info.getMatchDay(),
                        info.getLeagueRound());
                    matches.put(match.getId(), match);
                    AssociationManager.getInstance().addCurrentMatch(match);
                }
                shiftTeams();
                roundDate = roundDate.plusDays(7);
            }
            LOG.info("League matches scheduled");
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("League.scheduleMatches", ex);
        }
    }

    public List<Match> getMatchesForDay(LocalDate currentDate) {
        if (currentDate != null) {
            return matches.values().stream().filter(match -> match.getMatchInfo().getMatchDay().compareTo(currentDate) == 0).toList();
        } else {
            ErrorUtils.raise("Called 'getMatchesForDay' with parameter NULL");
            return null;
        }
    }

    public List<Match> getMatchesForRound(int round) {
        if (round >= 0 && round <= getTotalRounds()) {
            return matches.values().stream().filter(match -> match.getMatchInfo().getLeagueRound() == round).toList();
        } else {
            ErrorUtils.raise(String.format("Called 'getRoundMatches' with parameter out-of-bounds (called %d, available %d)", round, getTotalRounds()));
            return null;
        }
    }

    public Result saveMatch(Match match) {
        try {
            includeMatchIntoStandings(match);
            LOG.info("Match {} included into league standings", match);
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("League.saveMatch", ex);
        }
    }

    // used when reassembling a loaded world
    public void restoreMatch(Match match) {
        matches.put(match.getId(), match);
    }

    /**
     * Restores a standing entry when reassembling a loaded world.
     *
     * @param standing standing entry to insert, keyed by its team ID
     */
    public void restoreStanding(Standing standing) {
        standings.put(standing.getTeamId(), standing);
    }

    public UUID[] getAdvancingTeams() {
        var sorted = getStandings();
        return new UUID[] {sorted.get(0).getTeamId(), sorted.get(1).getTeamId()};
    }

    public UUID[] getRelegatedTeams() {
        var sorted = getStandings();
        return new UUID[] {sorted.get(sorted.size() - 2).getTeamId(), sorted.get(sorted.size() - 1).getTeamId()};
    }

    ////////////////////////////////////////////////////////////////////////////

    private int getTotalRounds() {
        return (teams.size() - 1) * 4;
    }

    private void shiftTeams() {
        var shiftedTeams = new ArrayList<Team>();
        shiftedTeams.add(teams.get(0));
        shiftedTeams.add(teams.get((teams.size() - 1)));
        shiftedTeams.addAll(teams.subList(1, (teams.size() - 1)));
        teams = shiftedTeams;
    }

    private void includeMatchIntoStandings(Match match) {
        var homeTeamId = match.getHomeLineup().getLineupInfo().getTeamId();
        var homeTeamStanding = standings.get(homeTeamId);
        if (homeTeamStanding == null) {
            throw new IllegalStateException("Home team not found in league standings: " + homeTeamId);
        }

        var awayTeamId = match.getAwayLineup().getLineupInfo().getTeamId();
        var awayTeamStanding = standings.get(awayTeamId);
        if (awayTeamStanding == null) {
            throw new IllegalStateException("Away team not found in league standings: " + awayTeamId);
        }

        homeTeamStanding.setGames(homeTeamStanding.getGames() + 1);
        awayTeamStanding.setGames(awayTeamStanding.getGames() + 1);

        int homeRuns = match.getBoxScore().getTotalPoints(false);
        int awayRuns = match.getBoxScore().getTotalPoints(true);

        homeTeamStanding.setRunsFor(homeTeamStanding.getRunsFor() + homeRuns);
        homeTeamStanding.setRunsAgainst(homeTeamStanding.getRunsAgainst() + awayRuns);

        awayTeamStanding.setRunsFor(awayTeamStanding.getRunsFor() + awayRuns);
        awayTeamStanding.setRunsAgainst(awayTeamStanding.getRunsAgainst() + homeRuns);

        if (homeRuns > awayRuns) {
            homeTeamStanding.setWins(homeTeamStanding.getWins() + 1);
            awayTeamStanding.setLoses(awayTeamStanding.getLoses() + 1);
        } else {
            homeTeamStanding.setLoses(homeTeamStanding.getLoses() + 1);
            awayTeamStanding.setWins(awayTeamStanding.getWins() + 1);
        }
    }
}
