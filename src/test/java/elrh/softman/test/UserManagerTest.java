package elrh.softman.test;

import elrh.softman.logic.core.Club;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.logic.managers.UserManager;
import org.junit.jupiter.api.*;

import static elrh.softman.test.utils.TestUtils.ELEMENT_NAME;
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
        assertFalse(user.userManagesTeam(1), "team 1 shouldn't be managed by the user yet");

        var club = new Club(ELEMENT_NAME, ELEMENT_NAME, ELEMENT_NAME);
        club.formTeam(PlayerLevel.MSEN);

        assertFalse(user.userManagesTeam(1), "team 1 shouldn't be managed by the user yet");

        user.setActiveClub(club);
        assertTrue(user.userManagesTeam(1), "team 1 should be managed by the user now");
    }

}
