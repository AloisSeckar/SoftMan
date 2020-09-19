package elrh.softman.mock;

import elrh.softman.db.DBManager;
import elrh.softman.logic.*;

public class MockTeamFactory {
    
    public static Team getMockHomeTeam() {
        Team homeTeam = new Team("RED TEAM");
        
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
    
    public static Team getMockAwayTeam() {
        Team awayTeam = new Team("BLUE TEAM");
        
        awayTeam.fillPosition(new Player(getRandomPlayerName(), 7), Position.FIRST_BASE, 0);
        awayTeam.fillPosition(new Player(getRandomPlayerName(), 23), Position.CATCHER, 1);
        awayTeam.fillPosition(new Player(getRandomPlayerName(), 20), Position.CENTER_FIELD, 2);
        awayTeam.fillPosition(new Player(getRandomPlayerName(), 44), Position.PITCHER, 3);
        awayTeam.fillPosition(new Player(getRandomPlayerName(), 17), Position.THIRD_BASE, 4);
        awayTeam.fillPosition(new Player(getRandomPlayerName(), 12), Position.LEFT_FIELD, 5);
        awayTeam.fillPosition(new Player(getRandomPlayerName(), 2), Position.SHORT_STOP, 6);
        awayTeam.fillPosition(new Player(getRandomPlayerName(), 3), Position.SECOND_BASE, 7);
        awayTeam.fillPosition(new Player(getRandomPlayerName(), 15), Position.RIGHT_FIELD, 8);
        
        awayTeam.addSubtitute(new Player(getRandomPlayerName(), 9));
        awayTeam.addSubtitute(new Player(getRandomPlayerName(), 56));
        awayTeam.addSubtitute(new Player(getRandomPlayerName(), 32));
        awayTeam.addSubtitute(new Player(getRandomPlayerName(), 80));
        
        return awayTeam;     
    }
    
    ////////////////////////////////////////////////////////////////////////////
    private static String getRandomPlayerName() {
        return DBManager.getInstance().getRandomFirstName() + " " + DBManager.getInstance().getRandomLastName();
    }
    
}
