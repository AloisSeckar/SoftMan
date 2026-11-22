package elrh.softman.test;

import elrh.softman.logic.Result;
import elrh.softman.logic.core.*;
import elrh.softman.logic.core.stats.Standing;
import elrh.softman.logic.core.data.LeagueInfo;
import elrh.softman.logic.core.data.MatchInfo;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.test.utils.TestUtils;
import static elrh.softman.test.utils.TestUtils.ELEMENT_NAME;
import elrh.softman.utils.Constants;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class LeagueTest {
    private static final Club CLUB = TestUtils.getTestClub();
    private static final java.util.UUID LEAGUE_ID = java.util.UUID.randomUUID();

    private League league;
    private Result result;

    @BeforeEach
    void setUp() {
        LeagueInfo info = new LeagueInfo(ELEMENT_NAME, PlayerLevel.MSEN, Constants.START_YEAR, 1, 1000);
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
        assertEquals(2, league.getMatchesForDay(League.LEAGUE_START.atYear(Constants.START_YEAR)).size(), "there should be 2 matches at 1st day");
    }

    @Test
    @DisplayName("scheduleMatchesFutureSeasonTest")
    void scheduleMatchesFutureSeasonTest() {
        int nextYear = Constants.START_YEAR + 1;
        var futureLeague = new League(new LeagueInfo(ELEMENT_NAME, PlayerLevel.MSEN, nextYear, 1, 1000));
        futureLeague.registerTeam(new Team(PlayerLevel.MSEN, ELEMENT_NAME, CLUB));
        futureLeague.registerTeam(new Team(PlayerLevel.MSEN, ELEMENT_NAME, CLUB));

        result = futureLeague.scheduleMatches();
        assertTrue(result.ok(), "scheduling future season matches should be successful");
        assertEquals(0, futureLeague.getMatchesForDay(League.LEAGUE_START.atYear(Constants.START_YEAR)).size(),
            "future season should not have matches in the initial year");
        assertEquals(1, futureLeague.getMatchesForDay(League.LEAGUE_START.atYear(nextYear)).size(),
            "future season should start on LEAGUE_START of its own year");
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
        var sTeam1 = new Standing(LEAGUE_ID, team1.getId(), team1.getName());
        sTeam1.setGames(3);
        sTeam1.setWins(2);
        sTeam1.setLoses(1);
        sTeam1.setRunsFor(10);
        sTeam1.setRunsAgainst(5);
        league.restoreStanding(sTeam1);

        // should be 3rd
        var sTeam2 = new Standing(LEAGUE_ID, team2.getId(), team2.getName());
        sTeam2.setGames(3);
        sTeam2.setWins(2);
        sTeam2.setLoses(1);
        sTeam2.setRunsFor(8);
        sTeam2.setRunsAgainst(9);
        league.restoreStanding(sTeam2);

        // should be 1st
        var sTeam3 = new Standing(LEAGUE_ID, team3.getId(), team3.getName());
        sTeam3.setGames(3);
        sTeam3.setWins(3);
        sTeam3.setLoses(0);
        sTeam3.setRunsFor(15);
        sTeam3.setRunsAgainst(5);
        league.restoreStanding(sTeam3);

        // should be 6th
        var sTeam4 = new Standing(LEAGUE_ID, team4.getId(), team4.getName());
        sTeam4.setGames(3);
        sTeam4.setWins(0);
        sTeam4.setLoses(3);
        sTeam4.setRunsFor(4);
        sTeam4.setRunsAgainst(17);
        league.restoreStanding(sTeam4);

        // should be 4th
        var sTeam5 = new Standing(LEAGUE_ID, team5.getId(), team5.getName());
        sTeam5.setGames(3);
        sTeam5.setWins(1);
        sTeam5.setLoses(2);
        sTeam5.setRunsFor(7);
        sTeam5.setRunsAgainst(8);
        league.restoreStanding(sTeam5);

        // should be 5th
        var sTeam6 = new Standing(LEAGUE_ID, team6.getId(), team6.getName());
        sTeam6.setGames(3);
        sTeam6.setWins(1);
        sTeam6.setLoses(2);
        sTeam6.setRunsFor(9);
        sTeam6.setRunsAgainst(14);
        league.restoreStanding(sTeam6);

        var advancing = league.getAdvancingTeams();
        assertEquals(advancing[0], team3.getId(), "Team 3 should be 1st");
        assertEquals(advancing[1], team1.getId(), "Team 1 should be 2nd");

        var relegated = league.getRelegatedTeams();
        assertEquals(relegated[0], team6.getId(), "Team 6 should be 5th");
        assertEquals(relegated[1], team4.getId(), "Team 4 should be 6th");

    }

    @Test
    @DisplayName("saveMatchMoreThanTenTeamsTest")
    void saveMatchMoreThanTenTeamsTest() {
        var teams = new ArrayList<Team>();
        for (int i = 1; i <= 12; i++) {
            var team = new Team(PlayerLevel.MSEN, ELEMENT_NAME + i, CLUB);
            league.registerTeam(team);
            teams.add(team);
        }

        var awayTeam = teams.get(10);
        var homeTeam = teams.get(11);
        var match = new Match(new MatchInfo(), awayTeam.getDefaultLineup(), homeTeam.getDefaultLineup());
        match.getBoxScore().addPoint(1, false);
        match.getBoxScore().addPoint(2, false);
        match.getBoxScore().addPoint(1, true);

        result = league.saveMatch(match);
        assertTrue(result.ok(), "saving a match for teams past index 9 should succeed");

        var homeStanding = league.getStandings().stream()
            .filter(s -> homeTeam.getId().equals(s.getTeamId()))
            .findFirst()
            .orElseThrow();
        var awayStanding = league.getStandings().stream()
            .filter(s -> awayTeam.getId().equals(s.getTeamId()))
            .findFirst()
            .orElseThrow();

        assertEquals(1, homeStanding.getGames());
        assertEquals(1, homeStanding.getWins());
        assertEquals(0, homeStanding.getLoses());
        assertEquals(2, homeStanding.getRunsFor());
        assertEquals(1, homeStanding.getRunsAgainst());

        assertEquals(1, awayStanding.getGames());
        assertEquals(0, awayStanding.getWins());
        assertEquals(1, awayStanding.getLoses());
        assertEquals(1, awayStanding.getRunsFor());
        assertEquals(2, awayStanding.getRunsAgainst());
    }

    @Test
    @DisplayName("saveMatchUnregisteredTeamTest")
    void saveMatchUnregisteredTeamTest() {
        var registered = new Team(PlayerLevel.MSEN, ELEMENT_NAME + "1", CLUB);
        var unregistered = new Team(PlayerLevel.MSEN, ELEMENT_NAME + "2", CLUB);
        league.registerTeam(registered);

        var match = new Match(new MatchInfo(), unregistered.getDefaultLineup(), registered.getDefaultLineup());
        result = league.saveMatch(match);
        assertFalse(result.ok(), "saving a match with an unregistered team should fail cleanly");
    }

}
