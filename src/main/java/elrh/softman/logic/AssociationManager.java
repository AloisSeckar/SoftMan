package elrh.softman.logic;

import elrh.softman.db.GameDBManager;
import elrh.softman.mock.MockTeamFactory;
import java.util.*;
import lombok.*;

public class AssociationManager {

    private final List<League> managedLeagues = new ArrayList<>();

    @Getter
    @Setter
    private League playerLeague;

    @Getter
    @Setter
    private Team playerTeam;

    private static AssociationManager INSTANCE;

    private AssociationManager() {
        prepareLeagues();
    }

    public static AssociationManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AssociationManager();
        }
        return INSTANCE;
    }

    ////////////////////////////////////////////////////////////////////////////
    private void prepareLeagues() {
        Team testTeam = MockTeamFactory.getMockTeam("REDS");

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(testTeam);
        teams.add(MockTeamFactory.getMockTeam("BLUES"));
        teams.add(MockTeamFactory.getMockTeam("GREENS"));
        teams.add(MockTeamFactory.getMockTeam("YELLOWS"));
        teams.add(MockTeamFactory.getMockTeam("BLACKS"));
        teams.add(MockTeamFactory.getMockTeam("WHITES"));
        teams.add(MockTeamFactory.getMockTeam("SILVERS"));
        teams.add(MockTeamFactory.getMockTeam("VIOLETS"));
        teams.add(MockTeamFactory.getMockTeam("BROWNS"));
        teams.add(MockTeamFactory.getMockTeam("GOLDS"));

        League testLeague = new League("Test league", teams);
        GameDBManager.getInstance().saveLeague(testLeague);

        managedLeagues.add(testLeague);
        playerLeague = testLeague;
        playerTeam = testTeam;
    }

}
