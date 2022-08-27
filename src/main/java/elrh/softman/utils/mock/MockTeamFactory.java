package elrh.softman.utils.mock;

import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.db.SourcesDBManager;
import elrh.softman.logic.enums.LeagueLevel;
import java.util.*;

public class MockTeamFactory {
    
    public static Team getMockTeam(LeagueLevel level, Club club) {
        Team team = new Team(level, club);
        
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
