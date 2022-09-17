package elrh.softman.test;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.enums.PlayerLevel;
import static elrh.softman.test.utils.TestUtils.ELEMENT_NAME;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClubTest extends AbstractDBTest {

    private AssociationManager manager;
    private Club club;

    @BeforeEach
    void setUp() {
        manager = AssociationManager.getInstance();
        manager.reset();
        club = new Club(ELEMENT_NAME, ELEMENT_NAME, ELEMENT_NAME);
    }

    @Test
    @DisplayName("isActiveTest")
    void isActiveTest() {
        assertFalse(club.isActive(), "initially club shouldn't be active");
        manager.registerClub(club);
        assertTrue(club.isActive(), "club should be active after registration");
        manager.getClock().nextYear();
        assertFalse(club.isActive(), "club shouldn't be active after advancing to next year");
    }

    @Test
    @DisplayName("formTeamTest")
    void formTeamTest() {
        assertEquals(0, club.getTeams().size(), "initially there should be 0 teams in club");

        Result result = club.formTeam(PlayerLevel.MU18);
        assertTrue(result.ok(), "team 'MU18 A' should be formed");
        result = club.formTeam(PlayerLevel.MU18);
        assertTrue(result.ok(), "team 'MU18 B' should be formed");
        result = club.formTeam(PlayerLevel.MU18);
        assertTrue(result.ok(), "team 'MU18 C' should be formed");
        result = club.formTeam(PlayerLevel.MU18);
        assertFalse(result.ok(), "team 'MU18 D' shouldn't be formed (max 3 teams per level)");

        List<Team> teams = club.getTeams();
        assertEquals(3, teams.size(), "there should be exactly 3 team in club now");
        assertTrue(teams.get(0).getTeamInfo().getName().contains(ELEMENT_NAME), "1st team's name should contain club name ('" + ELEMENT_NAME + "')");
        assertTrue(teams.get(1).getTeamInfo().getName().endsWith("B"), "2nd team's name should end with 'B'");
        assertTrue(teams.get(2).getTeamInfo().getName().contains("U18"), "3rd team's name should contain 'U18'");
    }

    @Test
    @DisplayName("getTeamsTest")
    void getTeamsTest() {
        club.formTeam(PlayerLevel.MSEN);
        club.formTeam(PlayerLevel.MSEN);

        assertEquals(2, club.getTeams().size(), "there should be 2 teams in club");

        var teamIds = club.getTeamIds();
        assertTrue(teamIds.contains(2L), "there should be team with ID 2");
        assertFalse(teamIds.contains(3L), "there shouldn't be team with ID 3");
    }
}
