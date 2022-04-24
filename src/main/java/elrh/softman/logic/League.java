package elrh.softman.logic;

import elrh.softman.db.GameDBManager;
import elrh.softman.db.orm.LeagueInfo;
import elrh.softman.gui.view.tab.StandingsTab;
import java.util.ArrayList;
import lombok.Getter;

public class League {

    @Getter
    private final LeagueInfo leagueInfo;

    private ArrayList<Team> teams;

    @Getter
    private final ArrayList<Standing> standings = new ArrayList<>();

    public League(String name, ArrayList<Team> teams) {
        this.leagueInfo = new LeagueInfo(name);
        this.teams = teams;

        GameDBManager.getInstance().saveTeams(teams);

        teams.forEach(team -> standings.add(new Standing(team.getName())));
    }

    public void playLeague() {
        for (int i = 1; i <= 18; i++) {
            previewCurrentRound();
            playRound();
        }
    }


    public void playGame() {
        Match match = new Match(teams.get(0), teams.get(1));
        match.simulate();
        GameDBManager.getInstance().saveMatch(match);
    }

    public void playRound() {
        Match match;
        for (int i = 0; i < 5; i++) {
            if (leagueInfo.getRound() % 2 == 0) {
                match = new Match(teams.get(i), teams.get(9 - i));
            } else {
                match = new Match(teams.get(9 - i), teams.get(i));
            }
            match.simulate();
            GameDBManager.getInstance().saveMatch(match);

            includeMatchIntoStandings(match);
        }

        shiftTeams();
        leagueInfo.setRoundPlayed();
    }

    public void previewCurrentRound() {
        StringBuilder sb = new StringBuilder();
        sb.append("LEAGUE ROUND ").append(leagueInfo.getRound()).append("\n");
        for (int i = 0; i < 5; i++) {
            if (leagueInfo.getRound() % 2 == 0) {
                sb.append(teams.get(i).getName()).append(" @ ").append(teams.get(9 - i).getName()).append("\n");
            } else {
                sb.append(teams.get(9 - i).getName()).append(" @ ").append(teams.get(i).getName()).append("\n");
            }
        }
        sb.append("-----------------------");
        StandingsTab.getInstance().writeIntoConsole(sb.toString());
    }

    ////////////////////////////////////////////////////////////////////////////
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
