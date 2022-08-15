package elrh.softman.logic;

import elrh.softman.constants.Constants;
import elrh.softman.db.GameDBManager;
import elrh.softman.db.orm.LeagueInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import elrh.softman.db.orm.MatchInfo;
import elrh.softman.utils.ErrorUtils;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class League {

    @Getter
    private final LeagueInfo leagueInfo;

    private ArrayList<Team> teams;

    private final HashMap<Integer, Match> matches;
    private final HashMap<Integer, ArrayList<Match>> matchesInRounds;

    @Getter
    private final ArrayList<Standing> standings = new ArrayList<>();

    public League(String name, LeagueLevelEnum level, ArrayList<Team> teams) {
        LOG.info("League '" + name + "' is being set-up");

        this.leagueInfo = new LeagueInfo(name, level);
        this.teams = teams;
        teams.forEach(team -> standings.add(new Standing(team.getName())));
        GameDBManager.getInstance().saveTeams(teams);
        LOG.info("Teams prepared");

        matches = new HashMap<>();
        matchesInRounds = new HashMap<>();
        scheduleMatches();
        LOG.info("Matches scheduled");

        LOG.info("League '" + name + "' is was set-up");
    }

    public List<Match> getTodayMatches(LocalDate currentDate) {
        if (currentDate != null) {
            return matches.values().stream().filter(match -> match.getMatchInfo().getMatchDay().compareTo(currentDate) == 0).toList();
        } else {
            ErrorUtils.raise("Called 'getRoundMatches' with parameter NULL");
            return null;
        }
    }

    public List<Match> getRoundMatches(int round) {
        if (round >= 0 && round < matchesInRounds.size()) {
            return matchesInRounds.get(round);
        } else {
            ErrorUtils.raise("Called 'getRoundMatches' with parameter out-of-bounds (called " + round + ", available " + matchesInRounds.size() + ")");
            return null;
        }
    }

    public void saveMatch(Match match) {
        GameDBManager.getInstance().saveMatch(match);
        includeMatchIntoStandings(match);
        LOG.info("Match " + match + " saved and included into league standings");
    }

    public void mockPlayLeague(TextArea target) {
        for (int i = 1; i <= 18; i++) {
            mockPreviewCurrentRound(target);
            mockPlayRound(target);
        }
    }

    public Match mockGetMatch() {
        return new Match(mockGetMatchInfo(), teams.get(0), teams.get(1));
    }

    public void mockPlayMatch(TextArea target) {
        var match = new Match(mockGetMatchInfo(), teams.get(0), teams.get(1));
        match.simulate(target);
        GameDBManager.getInstance().saveMatch(match);
    }

    private MatchInfo mockGetMatchInfo() {
        var ret = new MatchInfo();
        ret.setMatchId(1);
        ret.setMatchDay(LocalDate.now());
        return ret;
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

    public int getYear() {
        return leagueInfo.getYear();
    }

    ////////////////////////////////////////////////////////////////////////////

    private void scheduleMatches() {

        var matchIdBase = leagueInfo.getMatchId();
        var matchesPerRound = teams.size() / 2;
        var rounds = teams.size() * 4;
        var roundDate = LocalDate.of(Constants.START_YEAR,4,1);

        Collections.shuffle(teams);

        for (int i = 1; i <= rounds; i++) {
            matchesInRounds.put(i, new ArrayList<>(matchesPerRound));
            for (int j = 0; j < matchesPerRound; j++) {

                int matchId = matchIdBase + ((i - 1) * matchesPerRound) + (j + 1);

                int homeTeamIndex;
                int awayTeamIndex;
                if (i % 2 == 0) {
                    homeTeamIndex = j;
                    awayTeamIndex = 9 - j;
                } else {
                    homeTeamIndex = 9 - j;
                    awayTeamIndex = j;
                }

                var info = new MatchInfo();
                info.setMatchId(matchId);
                info.setMatchDay(roundDate);
                info.setLeagueRound(i);

                var match = new Match(info, teams.get(homeTeamIndex), teams.get(awayTeamIndex));
                LOG.info("Match: " + info.getMatchId() + " - " + match.getAwayTeam().getName() + " @ " + match.getHomeTeam().getName() + "; " + info.getMatchDay().toString() + " (rnd " + info.getLeagueRound() + ")");
                GameDBManager.getInstance().saveMatch(match);
                matches.put(matchId, match);
                (matchesInRounds.get(i)).add(match);
            }
            shiftTeams();
            roundDate = roundDate.plusDays(7);
        }
    }

    private void shiftTeams() {
        var shiftedTeams = new ArrayList<Team>();
        shiftedTeams.add(teams.get(0));
        shiftedTeams.add(teams.get(9));
        shiftedTeams.addAll(teams.subList(1, 9));
        teams = shiftedTeams;
    }

    private void includeMatchIntoStandings(Match match) {
        var homeTeamName = match.getHomeTeam().getName();
        Standing homeTeamStanding;
        int homeTeamIndex = -1;
        do {
            homeTeamIndex++;
            homeTeamStanding = standings.get(homeTeamIndex);
        } while (!homeTeamName.equals(homeTeamStanding.getTeam()) || homeTeamIndex > 9);

        var awayTeamName = match.getAwayTeam().getName();
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
