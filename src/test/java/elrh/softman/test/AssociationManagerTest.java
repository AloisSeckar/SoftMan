package elrh.softman.test;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.core.*;
import elrh.softman.logic.enums.PlayerGender;
import elrh.softman.logic.enums.PlayerLevel;
import static elrh.softman.test.utils.TestUtils.ELEMENT_NAME;
import elrh.softman.utils.Constants;
import elrh.softman.utils.factory.PlayerFactory;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AssociationManagerTest extends AbstractDBTest {

    private AssociationManager manager;
    private Result result;

    @BeforeEach
    void setUp() {
        manager = AssociationManager.getInstance();
        manager.reset();
    }

    @Test
    @DisplayName("createAndGetLeagueTest")
    void createAndGetLeagueTest() {
        result = manager.createNewLeague(ELEMENT_NAME, PlayerLevel.MSEN);
        assertTrue(result.ok(), "creating league should be successful");
        List<League> leagues = manager.getLeagues(Constants.START_YEAR);
        assertEquals(1, leagues.size(), "league should be registered and found");
        League testLeague = leagues.get(0);
        assertEquals(ELEMENT_NAME, testLeague.getLeagueInfo().getLeagueName(), "league name doesn't match");
    }

    @Test
    @DisplayName("registerTeamIntoLeagueTest")
    void registerTeamIntoLeague() {
        result = manager.createNewLeague(ELEMENT_NAME, PlayerLevel.MSEN);
        assertTrue(result.ok(), "creating league should be successful");
        var league = manager.getLeagues(Constants.START_YEAR).get(0);
        var leagueId = league.getId();
        result = manager.registerTeamIntoLeague(leagueId, new Team(PlayerLevel.MSEN, "TestTeam", new Club(ELEMENT_NAME, ELEMENT_NAME, ELEMENT_NAME)));
        assertTrue(result.ok(), "registering team into league should be successful");
        var team = league.getTeams().get(0);
        assertNotNull(team, "a team should be presented in the league");
        assertEquals("TestTeam", team.getName(), "correct team should be presented in the league");
    }

    @Test
    @DisplayName("registerAndGetClubTest")
    void registerAndGetClubTest() {
        Club newClub1 = new Club(ELEMENT_NAME, ELEMENT_NAME, ELEMENT_NAME);
        newClub1.getClubInfo().setClubId(1);
        Club newClub2 = new Club(ELEMENT_NAME, ELEMENT_NAME, ELEMENT_NAME);
        newClub2.getClubInfo().setClubId(2);

        result = manager.registerClub(newClub1);
        assertTrue(result.ok(), "registering club 1 should be successful");
        List<Club> clubs = manager.getClubs(true);
        assertEquals(1, clubs.size(), "club should be registered and found");
        Club testClub = clubs.get(0);
        assertEquals(ELEMENT_NAME, testClub.getClubInfo().getName(), "club name doesn't match");

        result = manager.registerClub(newClub2);
        assertTrue(result.ok(), "registering club 2 should be successful");
        assertEquals(2, manager.getClubs(true).size(), "there should be exactly 2 clubs registered");

        Club testClubById = manager.getClubById(testClub.getId());
        assertNotNull(testClubById, "club wasn't find by ID");
        assertEquals(testClub, testClubById, "club found by ID is not the same as expected");

        manager.getClock().nextYear();
        assertEquals(0, manager.getClubs(true).size(),"no clubs should be active next year");
        result = manager.registerClub(newClub1);
        assertTrue(result.ok(), "registering club 1 for next year should be successful");
        assertEquals(1, manager.getClubs(true).size(), "only 1 club should be active after re-registration");
        assertEquals(2, manager.getClubs(false).size(), "there still should be exactly 2 clubs managed");
    }

    @Test
    @DisplayName("registerAndGetPlayerTest")
    void registerAndGetPlayerTest() {
        // TODO use some player factory method...
        Player newPlayer1 = new Player();
        newPlayer1.setPlayerInfo(PlayerFactory.getRandomPlayerInfo(PlayerGender.M, 2000, 1));
        newPlayer1.getPlayerInfo().setPlayerId(1);
        newPlayer1.getPlayerInfo().setName(ELEMENT_NAME);
        Player newPlayer2 = new Player();
        newPlayer2.setPlayerInfo(PlayerFactory.getRandomPlayerInfo(PlayerGender.M, 2000, 2));
        newPlayer2.getPlayerInfo().setPlayerId(2);
        newPlayer2.getPlayerInfo().setName(ELEMENT_NAME);

        result = manager.registerPlayer(newPlayer1);
        assertTrue(result.ok(), "registering player 1 should be successful");
        List<Player> Players = manager.getPlayers(true);
        assertEquals(1, Players.size(), "player should be registered and found");
        Player testPlayer = Players.get(0);
        assertEquals(ELEMENT_NAME, testPlayer.getPlayerInfo().getName(), "player name doesn't match");

        result = manager.registerPlayer(newPlayer2);
        assertTrue(result.ok(), "registering player 2 should be successful");
        assertEquals(2, manager.getPlayers(true).size(), "there should be exactly 2 players registered");

        Player testPlayerById = manager.getPlayerById(testPlayer.getId());
        assertNotNull(testPlayerById, "player wasn't find by ID");
        assertEquals(testPlayer, testPlayerById, "player found by ID is not the same as expected");

        manager.getClock().nextYear();
        assertEquals(0, manager.getPlayers(true).size(),"no players should be active next year");
        result = manager.registerPlayer(newPlayer1);
        assertTrue(result.ok(), "registering player 1 for next year should be successful");
        assertEquals(1, manager.getPlayers(true).size(), "only 1 player should be active after re-registration");
        assertEquals(2, manager.getPlayers(false).size(), "there still should be exactly 2 players managed");
    }

    @Test
    @DisplayName("getMatchesTest")
    void getMatchesTest() {
        // TODO test following methods
        //  getDailyMatches
        //  getDailyMatchesForUser
        //  getDailyMatchesForLeague
        //  getRoundMatchesForLeague
    }

}
