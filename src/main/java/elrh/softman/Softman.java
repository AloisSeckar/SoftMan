package elrh.softman;

import elrh.softman.logic.*;
import elrh.softman.mock.MockTeamFactory;

public class Softman {
    
    public static void main(String[] args) {
        
        Team homeTeam = MockTeamFactory.getMockHomeTeam();
        Team awayTeam = MockTeamFactory.getMockAwayTeam();
        
        MatchSimulator.simulateMatch(awayTeam, homeTeam);
        
    }
    
}
