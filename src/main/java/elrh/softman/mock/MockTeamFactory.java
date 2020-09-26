package elrh.softman.mock;

import elrh.softman.db.SourcesDBManager;
import elrh.softman.logic.*;

public class MockTeamFactory {
    
    public static Team getMockTeam(String name) {
        Team homeTeam = new Team(name);
        
        homeTeam.fillPosition(new Player(getRandomPlayerName(), 11), Position.RIGHT_FIELD, 0);
        homeTeam.fillPosition(new Player(getRandomPlayerName(), 6), Position.CATCHER, 1);
        homeTeam.fillPosition(new Player(getRandomPlayerName(), 21), Position.DESIGNATED_HITTER, 2);
        homeTeam.fillPosition(new Player(getRandomPlayerName(), 10), Position.SECOND_BASE, 3);
        homeTeam.fillPosition(new Player(getRandomPlayerName(), 43), Position.CENTER_FIELD, 4);
        homeTeam.fillPosition(new Player(getRandomPlayerName(), 7), Position.THIRD_BASE, 5);
        homeTeam.fillPosition(new Player(getRandomPlayerName(), 1), Position.SHORT_STOP, 6);
        homeTeam.fillPosition(new Player(getRandomPlayerName(), 12), Position.FIRST_BASE, 7);
        homeTeam.fillPosition(new Player(getRandomPlayerName(), 15), Position.LEFT_FIELD, 8);
        homeTeam.fillPosition(new Player(getRandomPlayerName(), 38), Position.PITCHER, 9);
        
        homeTeam.addSubtitute(new Player(getRandomPlayerName(), 4));
        homeTeam.addSubtitute(new Player(getRandomPlayerName(), 73));
        
        return homeTeam;     
    }
    
    ////////////////////////////////////////////////////////////////////////////
    private static String getRandomPlayerName() {
        return SourcesDBManager.getInstance().getRandomFirstName() + " " + SourcesDBManager.getInstance().getRandomLastName();
    }
    
}
