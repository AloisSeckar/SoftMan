package elrh.softman.test;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.core.*;
import elrh.softman.logic.enums.PlayerGender;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.test.utils.TestUtils;
import static elrh.softman.test.utils.TestUtils.ELEMENT_NAME;
import elrh.softman.utils.Constants;
import elrh.softman.utils.factory.PlayerFactory;
import java.time.LocalDate;
import elrh.softman.utils.factory.TeamFactory;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class AssociationManagerTest extends AbstractDBTest {

    private AssociationManager manager;
    private Result result;

    @BeforeEach
    void setUp() {
        manager = AssociationManager.getInstance();
        manager.reset();
        manager.setTestMode(true);
    }

    @Test
    @DisplayName("createAndGetLeagueTest")
    void createAndGetLeagueTest() {
        result = manager.createNewLeague(ELEMENT_NAME, PlayerLevel.MSEN, 1);
        assertTrue(result.ok(), "creating league should be successful");
        var leagues = manager.getLeagues(Constants.START_YEAR);
        assertEquals(1, leagues.size(), "league should be registered and found");
        var testLeague = leagues.get(0);
        assertEquals(ELEMENT_NAME, testLeague.getLeagueInfo().getLeagueName(), "league name doesn't match");
    }

    @Test
    @DisplayName("registerTeamIntoLeagueTest")
    void registerTeamIntoLeague() {
        result = manager.createNewLeague(ELEMENT_NAME, PlayerLevel.MSEN, 1);
        assertTrue(result.ok(), "creating league should be successful");
        var league = manager.getLeagues(Constants.START_YEAR).get(0);
        var leagueId = league.getId();
        result = manager.registerTeamIntoLeague(leagueId, new Team(PlayerLevel.MSEN, "TestTeam", TestUtils.getTestClub()));
        assertTrue(result.ok(), "registering team into league should be successful");
        var team = league.getTeams().get(0);
        assertNotNull(team, "a team should be presented in the league");
        assertEquals("TestTeam", team.getName(), "correct team should be presented in the league");
        var teamId = team.getId();
        team = manager.getTeamById(teamId);
        assertNotNull(team, "a team should be found among registered teams");
        assertEquals("TestTeam", team.getName(), "correct team should be found among registered teams");
    }

    @Test
    @DisplayName("registerAndGetClubTest")
    void registerAndGetClubTest() {
        var newClub1 = TestUtils.getTestClub();
        newClub1.getClubInfo().setClubId(1);
        var newClub2 = TestUtils.getTestClub();
        newClub2.getClubInfo().setClubId(2);

        result = manager.registerClub(newClub1);
        assertTrue(result.ok(), "registering club 1 should be successful");
        var clubs = manager.getClubs(true);
        assertEquals(1, clubs.size(), "club should be registered and found");
        var testClub = clubs.get(0);
        assertEquals(ELEMENT_NAME, testClub.getClubInfo().getName(), "club name doesn't match");

        result = manager.registerClub(newClub2);
        assertTrue(result.ok(), "registering club 2 should be successful");
        assertEquals(2, manager.getClubs(true).size(), "there should be exactly 2 clubs registered");

        var testClubById = manager.getClubById(testClub.getId());
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
        var newPlayer1 = PlayerFactory.getRandomPlayer(PlayerGender.M, 2000, 1);
        newPlayer1.getPlayerInfo().setPlayerId(1);
        newPlayer1.getPlayerInfo().setName(ELEMENT_NAME);
        var newPlayer2 = PlayerFactory.getRandomPlayer(PlayerGender.M, 2000, 2);
        newPlayer2.getPlayerInfo().setPlayerId(2);
        newPlayer2.getPlayerInfo().setName(ELEMENT_NAME);

        result = manager.registerPlayer(newPlayer1);
        assertTrue(result.ok(), "registering player 1 should be successful");
        var players = manager.getPlayers(true);
        assertEquals(1, players.size(), "player should be registered and found");
        Player testPlayer = players.get(0);
        assertEquals(ELEMENT_NAME, testPlayer.getPlayerInfo().getName(), "player name doesn't match");

        result = manager.registerPlayer(newPlayer2);
        assertTrue(result.ok(), "registering player 2 should be successful");
        assertEquals(2, manager.getPlayers(true).size(), "there should be exactly 2 players registered");

        var testPlayerById = manager.getPlayerById(testPlayer.getId());
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
    @DisplayName("addAndGetTeamTest")
    void addAndGetTeamTest() {
        var club = TestUtils.getTestClub();
        var team = TeamFactory.getTeam(PlayerLevel.MSEN, ELEMENT_NAME + "_test", club);
        var id = team.getId();

        manager.addCurrentTeam(team);

        var retrieved = manager.getTeamById(id);
        assertNotNull(retrieved, "a team should be found");
        assertEquals(team.getName(), retrieved.getName(), "correct team should be found");
    }

    @Test
    @DisplayName("addAndGetMatchTest")
    void addAndGetMatchTest() {
        var match = TestUtils.getTestMatch();
        manager.addCurrentMatch(match);

        var retrieved = manager.getMatchById(match.getMatchInfo().getMatchId());
        assertNotNull(retrieved, "a team should be found");
        assertEquals(match.getMatchInfo().getMatchNumber(), retrieved.getMatchInfo().getMatchNumber(), "correct team should be found");
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

    @Test
    @DisplayName("isTodayMatchTest")
    void isTodayMatchTest() {
        initMatches();

        var match = manager.getLeagues(Constants.START_YEAR).get(0).getMatchesForRound(1).get(0);
        assertFalse(manager.isTodayMatch(match), "test match should be played tomorrow");
        result = manager.nextDay();
        assertTrue(manager.isTodayMatch(match), "test match should be played today");
        result = manager.nextDay();
        assertFalse(manager.isTodayMatch(match), "test match should be played yesterday");
    }

    @Test
    @DisplayName("nextDayTest")
    void nextDayTest() {
        initMatches();
        manager.nextDay();

        var match = manager.getLeagues(Constants.START_YEAR).get(0).getMatchesForRound(1).get(0);
        assertTrue(match.isScheduled(), "match should be scheduled");

        result = manager.nextDay();
        assertTrue(result.ok(), "advancing to next day should be successful");
        assertEquals(LocalDate.of(Constants.START_YEAR, 4, 2), manager.getClock().getCurrentDate(), "date should be 02.04." + Constants.START_YEAR);

        assertFalse(match.isScheduled(), "match shouldn't be scheduled");
        assertTrue(match.isFinished(), "match should be finished");
    }

    @Test
    @DisplayName("simulateUntilTest")
    void simulateUntilTest() {
        initMatches();

        var match1 = manager.getLeagues(Constants.START_YEAR).get(0).getMatchesForRound(1).get(0);
        var match2 = manager.getLeagues(Constants.START_YEAR).get(0).getMatchesForRound(2).get(0);
        var match3 = manager.getLeagues(Constants.START_YEAR).get(0).getMatchesForRound(3).get(0);
        var match4 = manager.getLeagues(Constants.START_YEAR).get(0).getMatchesForRound(4).get(0);

        var date1 = LocalDate.of(Constants.START_YEAR, 4, 10);
        result = manager.simulateUntil(date1);
        assertEquals(date1, manager.getClock().getCurrentDate(), "date should be 10.04." + Constants.START_YEAR);

        assertFalse(match1.isScheduled(), "1st match shouldn't be scheduled");
        assertFalse(match1.isScheduled(), "2nd match shouldn't be scheduled");
        assertTrue(match1.isFinished(), "1st match should be finished");
        assertTrue(match2.isFinished(), "2nd match should be finished");

        assertTrue(match3.isScheduled(), "3rd match should still be scheduled");
        assertTrue(match4.isScheduled(), "4th match should still be scheduled");
        assertFalse(match3.isFinished(), "3rd match shouldn't be finished yet");
        assertFalse(match4.isFinished(), "4th match shouldn't be finished yet");

        var date2 = LocalDate.of(Constants.START_YEAR, 4, 30);
        result = manager.simulateUntil(date2);
        assertEquals(date2, manager.getClock().getCurrentDate(), "date should be 30.04." + Constants.START_YEAR);

        assertFalse(match3.isScheduled(), "3rd match shouldn't be scheduled");
        assertFalse(match4.isScheduled(), "4th match shouldn't be scheduled");
        assertTrue(match3.isFinished(), "3rd match should be finished");
        assertTrue(match4.isFinished(), "4th match should be finished");
    }

    private void initMatches() {
        var club1 = TestUtils.getTestClub();
        club1.formTeam(PlayerLevel.MSEN);
        var club2 = TestUtils.getTestClub();
        club2.formTeam(PlayerLevel.MSEN);

        manager.createNewLeague(ELEMENT_NAME, PlayerLevel.MSEN, 1);

        var league = manager.getLeagues(Constants.START_YEAR).get(0);
        manager.registerTeamIntoLeague(league.getId(), club1.getTeams().get(0));
        manager.registerTeamIntoLeague(league.getId(), club2.getTeams().get(0));
        league.scheduleMatches();
    }

}
