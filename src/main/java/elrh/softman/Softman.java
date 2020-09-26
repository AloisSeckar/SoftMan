package elrh.softman;

import elrh.softman.db.*;
import elrh.softman.logic.*;
import elrh.softman.mock.MockTeamFactory;
import java.util.ArrayList;
import java.util.Arrays;

public class Softman {

    public static void main(String[] args) {

        try {

            String gameId = "test";
            GameDBManager.getInstance().setConnection(gameId);

            ArrayList<Team> teams = new ArrayList<>();
            teams.add(MockTeamFactory.getMockTeam("REDS"));
            teams.add(MockTeamFactory.getMockTeam("BLUES"));
            teams.add(MockTeamFactory.getMockTeam("GREENS"));
            teams.add(MockTeamFactory.getMockTeam("YELLOWS"));
            teams.add(MockTeamFactory.getMockTeam("BLACKS"));
            teams.add(MockTeamFactory.getMockTeam("WHITES"));
            teams.add(MockTeamFactory.getMockTeam("SILVERS"));
            teams.add(MockTeamFactory.getMockTeam("VIOLETS"));
            teams.add(MockTeamFactory.getMockTeam("BROWNS"));
            teams.add(MockTeamFactory.getMockTeam("GOLDS"));
            
            League testLeague = new League(teams);
            testLeague.playLeague();

        } finally {
            SourcesDBManager.getInstance().closeConnection();
            GameDBManager.getInstance().closeConnection();
        }

    }

}
