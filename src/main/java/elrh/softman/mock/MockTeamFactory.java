package elrh.softman.mock;

import elrh.softman.db.orm.PlayerInfo;
import elrh.softman.db.SourcesDBManager;
import elrh.softman.logic.*;
import java.util.*;

public class MockTeamFactory {
    
    public static Team getMockTeam(String name) {
        Team team = new Team(name);
        
        Random rand = new Random();
        List<Integer> usedNumbers = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            int number;
            do {
                number = rand.nextInt(99) + 1;
            } while (usedNumbers.contains(number));
            usedNumbers.add(number);
            team.addPlayer(new PlayerInfo(getRandomPlayerName(), number));
        }
        
        team.randomizeLineup();
        
        return team;     
    }
    
    ////////////////////////////////////////////////////////////////////////////
    private static String getRandomPlayerName() {
        return SourcesDBManager.getInstance().getRandomFirstName() + " " + SourcesDBManager.getInstance().getRandomLastName();
    }
    
}
