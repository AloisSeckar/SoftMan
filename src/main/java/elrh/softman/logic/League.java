package elrh.softman.logic;

import elrh.softman.db.GameDBManager;
import elrh.softman.db.orm.LeagueInfo;
import elrh.softman.gui.view.MainView;
import java.util.ArrayList;
import lombok.Getter;

public class League {
    
    @Getter
    private final LeagueInfo leagueInfo;
    
    private ArrayList<Team> teams = new ArrayList<>();
    
    public League(String name, ArrayList<Team> teams) {
        this.leagueInfo = new LeagueInfo(name);
        this.teams = teams;
        
        GameDBManager.getInstance().saveTeams(teams);
    }
    
    public void playLeague() {
        for (int i = 1; i <= 9; i++) {
            previewCurrentRound();
            playRound();
        }
    }
    
    public void playRound() {
        Match match;
        for (int i = 0; i < 5; i ++) {
            if (leagueInfo.getRound() % 2 == 0) {
                match = new Match(teams.get(i), teams.get(9 - i));
            } else {
                match = new Match(teams.get(9 - i), teams.get(i));
            }
            match.simulate();
            GameDBManager.getInstance().saveMatch(match);
        }
        
        shiftTeams();
        leagueInfo.setRoundPlayed();
    }
    
    public void previewCurrentRound() {
        StringBuilder sb = new StringBuilder();
        sb.append("LEAGUE ROUND ").append(leagueInfo.getRound()).append("\n");
        for (int i = 0; i < 5; i ++) {
            if (leagueInfo.getRound() % 2 == 0) {
                sb.append(teams.get(i).getName()).append(" @ ").append(teams.get(9 - i).getName()).append("\n");
            } else {
                sb.append(teams.get(9 - i).getName()).append(" @ ").append(teams.get(i).getName()).append("\n");
            }
        }
        sb.append("-----------------------");
        MainView.getInstance().writeIntoConsole(sb.toString());
    }

    ////////////////////////////////////////////////////////////////////////////
    private void shiftTeams() {
        ArrayList<Team> shiftedTeams = new ArrayList<>();
        shiftedTeams.add(teams.get(0));
        shiftedTeams.add(teams.get(9));
        shiftedTeams.addAll(teams.subList(1, 9));
        teams = shiftedTeams;
    }
    
}
