package elrh.softman.mock;

import elrh.softman.logic.*;

public class MockTeamFactory {
    
    public static Team getMockHomeTeam() {
        Team homeTeam = new Team("RED TEAM");
        
        homeTeam.fillPosition(new Player("John Doe", 11), Position.RIGHT_FIELD, 0);
        homeTeam.fillPosition(new Player("Tim Cole", 6), Position.CATCHER, 1);
        homeTeam.fillPosition(new Player("Sean Green", 21), Position.DESIGNATED_HITTER, 2);
        homeTeam.fillPosition(new Player("Fred Faut", 10), Position.SECOND_BASE, 3);
        homeTeam.fillPosition(new Player("Michael Elk", 43), Position.CENTER_FIELD, 4);
        homeTeam.fillPosition(new Player("Richard Bolster", 7), Position.THIRD_BASE, 5);
        homeTeam.fillPosition(new Player("Herman Gardner", 1), Position.SHORT_STOP, 6);
        homeTeam.fillPosition(new Player("Isiah Horn", 12), Position.FIRST_BASE, 7);
        homeTeam.fillPosition(new Player("Malcolm Porte", 15), Position.LEFT_FIELD, 8);
        homeTeam.fillPosition(new Player("Chris McAvern", 38), Position.PITCHER, 9);
        
        homeTeam.addSubtitute(new Player("Blake Elis", 4));
        homeTeam.addSubtitute(new Player("Stephen Cowley", 73));
        
        return homeTeam;     
    }
    
    public static Team getMockAwayTeam() {
        Team awayTeam = new Team("BLUE TEAM");
        
        awayTeam.fillPosition(new Player("Hector Delgado", 7), Position.FIRST_BASE, 0);
        awayTeam.fillPosition(new Player("Jose Salinas", 23), Position.CATCHER, 1);
        awayTeam.fillPosition(new Player("Pedro Gomez", 20), Position.CENTER_FIELD, 2);
        awayTeam.fillPosition(new Player("Carlos Guttierez", 44), Position.PITCHER, 3);
        awayTeam.fillPosition(new Player("Manuel Sandoval", 17), Position.THIRD_BASE, 4);
        awayTeam.fillPosition(new Player("Louis Rodriguez", 12), Position.LEFT_FIELD, 5);
        awayTeam.fillPosition(new Player("Evandro Salaz", 2), Position.SHORT_STOP, 6);
        awayTeam.fillPosition(new Player("Hector Ramirez", 3), Position.SECOND_BASE, 7);
        awayTeam.fillPosition(new Player("Oliver De La Vega", 15), Position.RIGHT_FIELD, 8);
        
        awayTeam.addSubtitute(new Player("Pedro Vasquez", 9));
        awayTeam.addSubtitute(new Player("Victor Arazinga", 56));
        awayTeam.addSubtitute(new Player("Eduardo Molinero", 32));
        awayTeam.addSubtitute(new Player("Jose Santacruz", 80));
        
        return awayTeam;     
    }
    
}
