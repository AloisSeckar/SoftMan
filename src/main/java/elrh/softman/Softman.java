package elrh.softman;

import elrh.softman.db.*;
import elrh.softman.logic.*;
import elrh.softman.mock.MockTeamFactory;

public class Softman {

    public static void main(String[] args) {

        try {

            String gameId = "test";
            GameDBManager.getInstance().setConnection(gameId);

            Team homeTeam = MockTeamFactory.getMockHomeTeam();
            Team awayTeam = MockTeamFactory.getMockAwayTeam();

            for (int i = 0; i < 2; i++) {
                Match testMatch = new Match(awayTeam, homeTeam);
                testMatch.simulate();

                GameDBManager.getInstance().saveMatch(testMatch);
            }

        } finally {
            SourcesDBManager.getInstance().closeConnection();
            GameDBManager.getInstance().closeConnection();
        }

    }

}
