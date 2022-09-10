package elrh.softman.test;

import elrh.softman.logic.Result;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.League;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.db.orm.LeagueInfo;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LeagueTest {

    private static final String ELEMENT_NAME = "Test";

    private static final Club CLUB = new Club(ELEMENT_NAME, ELEMENT_NAME, ELEMENT_NAME);

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

}
