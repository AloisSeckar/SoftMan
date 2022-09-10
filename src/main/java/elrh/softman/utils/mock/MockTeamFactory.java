package elrh.softman.utils.mock;

import elrh.softman.logic.core.*;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.utils.Constants;
import elrh.softman.utils.factory.PlayerFactory;
import java.util.*;

public class MockTeamFactory {
    
    public static Team getMockTeam(PlayerLevel level, Club club) {
        Team team = new Team(level, "MOCK", club);

        Random rand = new Random();
        List<Integer> usedNumbers = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            int number;
            do {
                number = rand.nextInt(99) + 1;
            } while (usedNumbers.contains(number));
            usedNumbers.add(number);
            team.addPlayer(PlayerFactory.getRandomPlayerInfo(level.getGender(), getRandomBirth(level), number));
        }
        
        team.randomizeLineup();
        
        return team;     
    }

    private static int getRandomBirth(PlayerLevel level) {
        if (level == PlayerLevel.MSEN || level == PlayerLevel.FSEN) {
            return Constants.START_YEAR - 16 - new Random().nextInt(25);
        } else {
            return Constants.START_YEAR - 12 - new Random().nextInt(6);
        }
    }

}
