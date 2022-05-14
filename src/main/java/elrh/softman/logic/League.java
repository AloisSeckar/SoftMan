package elrh.softman.logic;

import elrh.softman.db.GameDBManager;
import elrh.softman.db.orm.LeagueInfo;

import java.time.LocalDate;
import java.util.ArrayList;

import elrh.softman.db.orm.MatchInfo;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class League {

    @Getter
    private final LeagueInfo leagueInfo;

    private ArrayList<Team> teams;

    private ArrayList<Match> matches;

    @Getter
    private final ArrayList<Standing> standings = new ArrayList<>();

    public League(String name, ArrayList<Team> teams) {
        LOG.info("League '" + name + "' is being set-up");

        this.leagueInfo = new LeagueInfo(name);
        this.teams = teams;
        teams.forEach(team -> standings.add(new Standing(team.getName())));
        GameDBManager.getInstance().saveTeams(teams);
        LOG.info("Teams prepared");

        matches = scheduleMatches();
        LOG.info("Matches scheduled");

        LOG.info("League '" + name + "' is was set-up");
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
        Match match = new Match(mockGetMatchInfo(), teams.get(0), teams.get(1));
        match.simulate(target);
        GameDBManager.getInstance().saveMatch(match);
    }

    private MatchInfo mockGetMatchInfo() {
        MatchInfo ret = new MatchInfo();
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

    ////////////////////////////////////////////////////////////////////////////

    private ArrayList<Match> scheduleMatches() {
        var scheduledMatches = new ArrayList<Match>();
        var roundDate = LocalDate.of(2023,4,1);
        int rounds = teams.size() * 4;
        int matchIdBase = leagueInfo.getMatchId();
        for (int i = 1; i <= rounds; i++) {
            for (int j = 0; j < 5; j++) {

                int matchId = matchIdBase + i * (j + 1);

                int homeTeamIndex;
                int awayTeamIndex;
                if (i % 2 == 0) {
                    homeTeamIndex = j;
                    awayTeamIndex = 9 - j;
                } else {
                    homeTeamIndex = 9 - j;
                    awayTeamIndex = j;
                }

                MatchInfo info = new MatchInfo();
                info.setMatchId(matchId);
                info.setMatchDay(roundDate);
                info.setLeagueRound(i);

                Match match = new Match(info, teams.get(homeTeamIndex), teams.get(awayTeamIndex));
                scheduledMatches.add(match);
                GameDBManager.getInstance().saveMatch(match);
            }
            shiftTeams();
            roundDate = roundDate.plusDays(7);
        }
        return scheduledMatches;
    }

    private void shiftTeams() {
        ArrayList<Team> shiftedTeams = new ArrayList<>();
        shiftedTeams.add(teams.get(0));
        shiftedTeams.add(teams.get(9));
        shiftedTeams.addAll(teams.subList(1, 9));
        teams = shiftedTeams;
    }

    private void includeMatchIntoStandings(Match match) {
        String homeTeamName = match.getHomeTeam().getName();
        Standing homeTeamStanding;
        int homeTeamIndex = -1;
        do {
            homeTeamIndex++;
            homeTeamStanding = standings.get(homeTeamIndex);
        } while (!homeTeamName.equals(homeTeamStanding.getTeam()) || homeTeamIndex > 9);

        String awayTeamName = match.getAwayTeam().getName();
        Standing awayTeamStanding;
        int awayTeamIndex = -1;
        do {
            awayTeamIndex++;
            awayTeamStanding = standings.get(awayTeamIndex);
        } while (!awayTeamName.equals(awayTeamStanding.getTeam()) || awayTeamIndex > 9);

        homeTeamStanding.setGames(homeTeamStanding.getGames() + 1);
        awayTeamStanding.setGames(awayTeamStanding.getGames() + 1);

        int homeRuns = match.getBoxScore().getPoints(false);
        int awayRuns = match.getBoxScore().getPoints(true);

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
