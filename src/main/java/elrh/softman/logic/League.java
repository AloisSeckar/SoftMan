package elrh.softman.logic;

import elrh.softman.db.GameDBManager;
import java.util.ArrayList;
import lombok.Getter;

public class League {
    
    private ArrayList<Team> teams = new ArrayList<>();
    
    @Getter
    private int round = 1;
    
    public League(ArrayList<Team> teams) {
        this.teams = teams;
    }
    
    public void playLeague() {
        for (int i = 1; i <= 36; i++) {
            previewCurrentRound();
            playRound();
        }
    }
    
    public void playRound() {
        Match match;
        for (int i = 0; i < 5; i ++) {
            if (round % 2 == 0) {
                match = new Match(teams.get(i), teams.get(9 - i));
            } else {
                match = new Match(teams.get(9 - i), teams.get(i));
            }
            match.simulate();
            GameDBManager.getInstance().saveMatch(match);
        }
        
        shiftTeams();
        round++;
    }
    
    public void previewCurrentRound() {
        System.out.println("LEAGUE ROUND " + round);
        for (int i = 0; i < 5; i ++) {
            if (round % 2 == 0) {
                System.out.println(teams.get(i).getName() + " @ " + teams.get(9 - i).getName());
            } else {
                System.out.println(teams.get(9 - i).getName() + " @ " + teams.get(i).getName());
            }
        }
        System.out.println("-----------------------");
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
