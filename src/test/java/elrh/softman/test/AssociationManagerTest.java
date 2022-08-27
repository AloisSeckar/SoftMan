package elrh.softman.test;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.*;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.utils.Constants;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AssociationManagerTest {

    private static final String ELEMENT_NAME = "Test";

    private AssociationManager manager;

    @BeforeEach
    void setUp() {
        manager = AssociationManager.getInstance();
        manager.reset();
    }

    @Test
    @DisplayName("createAndGetLeagueTest")
    void createAndGetLeagueTest() {
        manager.createNewLeague(ELEMENT_NAME, PlayerLevel.MSEN);
        List<League> leagues = manager.getLeagues(Constants.START_YEAR);
        assertEquals(1, leagues.size(), "league should be registered and found");
        League testLeague = leagues.get(0);
        assertEquals(ELEMENT_NAME, testLeague.getLeagueInfo().getLeagueName(), "league name doesn't match");
    }

    @Test
    @DisplayName("registerTeamIntoLeagueTest")
    void registerTeamIntoLeague() {
        manager.createNewLeague(ELEMENT_NAME, PlayerLevel.MSEN);
        long leagueId = manager.getLeagues(Constants.START_YEAR).get(0).getId();
        manager.registerTeamIntoLeague(leagueId, new Team(PlayerLevel.MSEN, new Club(ELEMENT_NAME, ELEMENT_NAME, ELEMENT_NAME)));
        // TODO test if team was actually added - requries peek method in League
    }

    @Test
    @DisplayName("registerAndGetClubTest")
    void registerAndGetClubTest() {
        Club newClub1 = new Club(ELEMENT_NAME, ELEMENT_NAME, ELEMENT_NAME);
        newClub1.getClubInfo().setClubId(1);
        Club newClub2 = new Club(ELEMENT_NAME, ELEMENT_NAME, ELEMENT_NAME);
        newClub2.getClubInfo().setClubId(2);

        manager.registerClub(newClub1);
        List<Club> clubs = manager.getClubs(true);
        assertEquals(1, clubs.size(), "club should be registered and found");
        Club testClub = clubs.get(0);
        assertEquals(ELEMENT_NAME, testClub.getClubInfo().getName(), "club name doesn't match");

        manager.registerClub(newClub2);
        assertEquals(2, manager.getClubs(true).size(), "there should be exactly 2 clubs registered");

        Club testClubById = manager.getClubById(testClub.getId());
        assertNotNull(testClubById, "club wasn't find by ID");
        assertEquals(testClub, testClubById, "club found by ID is not the same as expected");

        manager.getClock().nextYear();
        assertEquals(0, manager.getClubs(true).size(),"no clubs should be active next year");
        manager.registerClub(newClub1);
        assertEquals(1, manager.getClubs(true).size(), "only 1 club should be active after re-registration");
        assertEquals(2, manager.getClubs(false).size(), "there still should be exactly 2 clubs managed");
    }

    @Test
    @DisplayName("registerAndGetPlayerTest")
    void registerAndGetPlayerTest() {
        Player newPlayer1 = new Player();
        newPlayer1.getPlayerInfo().setPlayerId(1);
        newPlayer1.getPlayerInfo().setName(ELEMENT_NAME);
        Player newPlayer2 = new Player();
        newPlayer2.getPlayerInfo().setPlayerId(2);
        newPlayer2.getPlayerInfo().setName(ELEMENT_NAME);

        manager.registerPlayer(newPlayer1);
        List<Player> Players = manager.getPlayers(true);
        assertEquals(1, Players.size(), "player should be registered and found");
        Player testPlayer = Players.get(0);
        assertEquals(ELEMENT_NAME, testPlayer.getPlayerInfo().getName(), "player name doesn't match");

        manager.registerPlayer(newPlayer2);
        assertEquals(2, manager.getPlayers(true).size(), "there should be exactly 2 players registered");

        Player testPlayerById = manager.getPlayerById(testPlayer.getId());
        assertNotNull(testPlayerById, "player wasn't find by ID");
        assertEquals(testPlayer, testPlayerById, "player found by ID is not the same as expected");

        manager.getClock().nextYear();
        assertEquals(0, manager.getPlayers(true).size(),"no players should be active next year");
        manager.registerPlayer(newPlayer1);
        assertEquals(1, manager.getPlayers(true).size(), "only 1 player should be active after re-registration");
        assertEquals(2, manager.getPlayers(false).size(), "there still should be exactly 2 players managed");
    }

}
