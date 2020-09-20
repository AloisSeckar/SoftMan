package elrh.softman;

import elrh.softman.db.GameDBManager;
import elrh.softman.db.SourcesDBManager;
import elrh.softman.logic.*;
import elrh.softman.mock.MockTeamFactory;
import java.util.UUID;

public class Softman {

    public static void main(String[] args) {

        try {
            
            GameDBManager.getInstance().setConnection(UUID.randomUUID().toString());
            
            Team homeTeam = MockTeamFactory.getMockHomeTeam();
            Team awayTeam = MockTeamFactory.getMockAwayTeam();

            Match testMatch = new Match(awayTeam, homeTeam);
            testMatch.simulate();

        } finally {
            SourcesDBManager.getInstance().closeConnection();
            GameDBManager.getInstance().closeConnection();
        }

    }

}
