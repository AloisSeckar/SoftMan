package elrh.softman.test;

import elrh.softman.logic.Result;
import elrh.softman.logic.core.*;
import elrh.softman.logic.core.stats.Standing;
import elrh.softman.logic.db.orm.LeagueInfo;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.test.utils.TestUtils;
import static elrh.softman.test.utils.TestUtils.ELEMENT_NAME;
import elrh.softman.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class LeagueTest extends AbstractDBTest {

    private static final Club CLUB = TestUtils.getTestClub();

    private League league;
    private Result result;

    @BeforeEach
    void setUp() {
        LeagueInfo info = new LeagueInfo(ELEMENT_NAME, PlayerLevel.MSEN, Constants.START_YEAR, 1000);
        league = new League(info);
    }

    @Test
    @DisplayName("registerTeamTest")
    void registerTeamTest() {
        assertEquals(0, league.getTeams().size(), "initially there should be no teams registered");
        Team teamMSEN = new Team(PlayerLevel.MSEN, ELEMENT_NAME, CLUB);
        result = league.registerTeam(teamMSEN);
        assertTrue(result.ok(), "registering team into league should be successful");
        assertEquals(1, league.getTeams().size(), "the team should have been registered into league");
    }

    @Test
    @DisplayName("scheduleMatchesTest")
    void scheduleMatchesTest() {
        Team team1 = new Team(PlayerLevel.MSEN, ELEMENT_NAME, CLUB);
        Team team2 = new Team(PlayerLevel.MSEN, ELEMENT_NAME, CLUB);
        Team team3 = new Team(PlayerLevel.MSEN, ELEMENT_NAME, CLUB);
        Team team4 = new Team(PlayerLevel.MSEN, ELEMENT_NAME, CLUB);
        league.registerTeam(team1);
        league.registerTeam(team2);
        league.registerTeam(team3);
        league.registerTeam(team4);
        result = league.scheduleMatches();
        assertTrue(result.ok(), "scheduling matches should be successful");
        assertEquals(2, league.getMatchesForRound(1).size(), "there should be 2 matches in 1st round");
        assertEquals(2, league.getMatchesForDay(League.LEAGUE_START).size(), "there should be 2 matches at 1st day");
    }

    @Test
    @DisplayName("standingsTest")
    void standingsTest() {
        var team1 = new Team(PlayerLevel.MSEN, ELEMENT_NAME + "1", CLUB);
        var team2 = new Team(PlayerLevel.MSEN, ELEMENT_NAME + "2", CLUB);
        var team3 = new Team(PlayerLevel.MSEN, ELEMENT_NAME + "3", CLUB);
        var team4 = new Team(PlayerLevel.MSEN, ELEMENT_NAME + "4", CLUB);
        var team5 = new Team(PlayerLevel.MSEN, ELEMENT_NAME + "5", CLUB);
        var team6 = new Team(PlayerLevel.MSEN, ELEMENT_NAME + "6", CLUB);

        // should be 2nd
        var sTeam1 = new Standing(team1.getId(), team1.getName());
        sTeam1.setGames(3);
        sTeam1.setWins(2);
        sTeam1.setLoses(1);
        sTeam1.setRunsFor(10);
        sTeam1.setRunsAgainst(5);
        league.getStandings().add(sTeam1);

        // should be 3rd
        var sTeam2 = new Standing(team2.getId(), team2.getName());
        sTeam2.setGames(3);
        sTeam2.setWins(2);
        sTeam2.setLoses(1);
        sTeam2.setRunsFor(8);
        sTeam2.setRunsAgainst(9);
        league.getStandings().add(sTeam2);

        // should be 1st
        var sTeam3 = new Standing(team3.getId(), team3.getName());
        sTeam3.setGames(3);
        sTeam3.setWins(3);
        sTeam3.setLoses(0);
        sTeam3.setRunsFor(15);
        sTeam3.setRunsAgainst(5);
        league.getStandings().add(sTeam3);

        // should be 6th
        var sTeam4 = new Standing(team4.getId(), team4.getName());
        sTeam4.setGames(3);
        sTeam4.setWins(0);
        sTeam4.setLoses(3);
        sTeam4.setRunsFor(4);
        sTeam4.setRunsAgainst(17);
        league.getStandings().add(sTeam4);

        // should be 4th
        var sTeam5 = new Standing(team5.getId(), team5.getName());
        sTeam5.setGames(3);
        sTeam5.setWins(1);
        sTeam5.setLoses(2);
        sTeam5.setRunsFor(7);
        sTeam5.setRunsAgainst(8);
        league.getStandings().add(sTeam5);

        // should be 5th
        var sTeam6 = new Standing(team6.getId(), team6.getName());
        sTeam6.setGames(3);
        sTeam6.setWins(1);
        sTeam6.setLoses(2);
        sTeam6.setRunsFor(9);
        sTeam6.setRunsAgainst(14);
        league.getStandings().add(sTeam6);

        var advancing = league.getAdvancingTeams();
        assertTrue(advancing[0] == team3.getId(), "Team 3 should be 1st");
        assertTrue(advancing[1] == team1.getId(), "Team 1 should be 2nd");

        var relegated = league.getRelegatedTeams();
        assertTrue(relegated[0] == team6.getId(), "Team 6 should be 5th");
        assertTrue(relegated[1] == team4.getId(), "Team 4 should be 6th");

    }

}
