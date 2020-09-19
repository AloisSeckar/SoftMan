package elrh.softman;

import elrh.softman.db.DBManager;
import elrh.softman.logic.*;
import elrh.softman.mock.MockTeamFactory;

public class Softman {

    public static void main(String[] args) {

        try {
            
            Team homeTeam = MockTeamFactory.getMockHomeTeam();
            Team awayTeam = MockTeamFactory.getMockAwayTeam();

            Match testMatch = new Match(awayTeam, homeTeam);
            testMatch.simulate();

        } finally {
            DBManager.getInstance().closeConnection();
        }

    }

}
