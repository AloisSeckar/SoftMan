package elrh.softman.logic;

import elrh.softman.db.GameDBManager;
import java.util.ArrayList;
import java.util.Arrays;

public class League {
    
    private ArrayList<Team> teams = new ArrayList<>();
    
    public League(ArrayList<Team> teams) {
        this.teams = teams;
    }
    
    public void playLeague() {
        for (int i = 1; i <= 9; i++) {
           
            playRound();
            
            shiftTeams();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    private void playRound() {
        Match match = new Match(teams.get(0), teams.get(9));
        match.simulate();
        GameDBManager.getInstance().saveMatch(match);

        match = new Match(teams.get(1), teams.get(8));
        match.simulate();
        GameDBManager.getInstance().saveMatch(match);

        match = new Match(teams.get(2), teams.get(7));
        match.simulate();
        GameDBManager.getInstance().saveMatch(match);

        match = new Match(teams.get(3), teams.get(6));
        match.simulate();
        GameDBManager.getInstance().saveMatch(match);

        match = new Match(teams.get(4), teams.get(5));
        match.simulate();
        GameDBManager.getInstance().saveMatch(match);
    }

    private void shiftTeams() {
        ArrayList<Team> shiftedTeams = new ArrayList<>();
        shiftedTeams.add(teams.get(0));
        shiftedTeams.add(teams.get(9));
        shiftedTeams.addAll(teams.subList(1, 9));
        teams = shiftedTeams;
    }
    
}
