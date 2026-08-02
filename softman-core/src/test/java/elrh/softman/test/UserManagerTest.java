package elrh.softman.test;

import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.logic.managers.UserManager;
import elrh.softman.test.utils.TestUtils;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest extends AbstractDBTest {

    private UserManager user;

    @BeforeEach
    void setUp() {
        user = new UserManager();
    }

    @Test
    @DisplayName("userManagesTeamTest")
    void userManagesTeamTest() {
        assertFalse(user.userManagesTeam(0), "team 0 shouldn't be managed by the user");

        var club = TestUtils.getTestClub();
        club.formTeam(PlayerLevel.MSEN);

        var teamId = club.getTeams().get(0).getId();

        assertFalse(user.userManagesTeam(teamId), "team shouldn't be managed by the user yet");

        user.setActiveClub(club);
        assertTrue(user.userManagesTeam(teamId), "team should be managed by the user now");
    }

}
