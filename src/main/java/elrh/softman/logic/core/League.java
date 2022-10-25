package elrh.softman.logic.core;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.utils.Constants;
import elrh.softman.logic.db.orm.LeagueInfo;
import elrh.softman.logic.db.orm.match.MatchInfo;
import elrh.softman.logic.core.stats.Standing;
import elrh.softman.utils.ErrorUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @RequiredArgsConstructor
public class League {

    public static final LocalDate LEAGUE_START = LocalDate.of(Constants.START_YEAR,4,1);

    @Getter
    @NonNull
    private final LeagueInfo leagueInfo;

    @Getter
    private ArrayList<Team> teams = new ArrayList<>();

    private final HashMap<Long, Match> matches = new HashMap<>();

    @Getter
    private final ArrayList<Standing> standings = new ArrayList<>();

    public long getId() {
        return leagueInfo.getLeagueId();
    }

    public String getName() {
        return leagueInfo.getLeagueName() + " " + leagueInfo.getYear();
    }

    public Result registerTeam(Team team) {
        try {
            teams.add(team);
            standings.add(new Standing(team.getName()));
            team.getTeamInfo().setLeagueInfo(this.leagueInfo);
            team.getTeamInfo().persist();
            LOG.info("Team " + team.getId() + " registered");
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
            var roundDate = LEAGUE_START;

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
                    LOG.info("Match: " + info.getMatchNumber() + " - " + match.getAwayLineup().getLinuepInfo().getTeamName() + " @ " + match.getHomeLineup().getLinuepInfo().getTeamName() + "; " + info.getMatchDay().toString() + " (rnd " + info.getLeagueRound() + ")");
                    var result = match.getMatchInfo().persist();
                    if (result.ok()) {
                        LOG.info("Saved into DB");
                    } else {
                        ErrorUtils.raise(result.message());
                    }
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
            ErrorUtils.raise("Called 'getRoundMatches' with parameter out-of-bounds (called " + round + ", available " + getTotalRounds() + ")");
            return null;
        }
    }

    public Result saveMatch(Match match) {
        try {
            match.getMatchInfo().persist();
            includeMatchIntoStandings(match);
            LOG.info("Match " + match + " saved and included into league standings");
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("League.saveMatch", ex);
        }
    }

    public void mockPlayLeague(TextArea target) {
        for (int i = 1; i <= 18; i++) {
            mockPreviewCurrentRound(target);
            mockPlayRound(target);
        }
    }

    public void mockPlayRound(TextArea target) {
        // TODO REMOVE
        target.appendText("MOCK BROKEN AND HOPEFULLY DEAD");
        /*
        Match match;
        for (int i = 0; i < 5; i++) {
            if (leagueInfo.getRound() % 2 == 0) {
                match = new Match(LocalDate.now(), teams.get(i), teams.get(9 - i));
            } else {
                match = new Match(LocalDate.now(), teams.get(9 - i), teams.get(i));
            }
            match.simulate(target);
            GameDBManager.getInstance().saveMatch(match);

            includeMatchIntoStandings(match);
        }

        shiftTeams();
        leagueInfo.setRoundPlayed();
         */
    }

    public void mockPreviewCurrentRound(TextArea target) {
        // TODO REMOVE
        target.appendText("MOCK BROKEN AND HOPEFULLY DEAD");
        /*
        StringBuilder sb = new StringBuilder();
        sb.append("LEAGUE ROUND ").append(leagueInfo.getRound()).append("\n");
        for (int i = 0; i < 5; i++) {
            if (leagueInfo.getRound() % 2 == 0) {
                sb.append(teams.get(i).getName()).append(" @ ").append(teams.get(9 - i).getName()).append("\n");
            } else {
                sb.append(teams.get(9 - i).getName()).append(" @ ").append(teams.get(i).getName()).append("\n");
            }
        }
        sb.append("-----------------------\n");
        target.appendText(sb.toString());
         */
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
        var homeTeamName = match.getHomeLineup().getLinuepInfo().getTeamName();
        Standing homeTeamStanding;
        int homeTeamIndex = -1;
        do {
            homeTeamIndex++;
            homeTeamStanding = standings.get(homeTeamIndex);
        } while (!homeTeamName.equals(homeTeamStanding.getTeam()) || homeTeamIndex > 9);

        var awayTeamName = match.getAwayLineup().getLinuepInfo().getTeamName();
        Standing awayTeamStanding;
        int awayTeamIndex = -1;
        do {
            awayTeamIndex++;
            awayTeamStanding = standings.get(awayTeamIndex);
        } while (!awayTeamName.equals(awayTeamStanding.getTeam()) || awayTeamIndex > 9);

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
