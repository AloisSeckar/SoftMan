package elrh.softman.test;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.core.*;
import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.enums.PlayerGender;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.utils.Constants;
import elrh.softman.utils.factory.PlayerFactory;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TeamTest extends AbstractDBTest {

    private static final String ELEMENT_NAME = "Test";
    private static final int AGE_30 = Constants.START_YEAR - 30;
    private static final int AGE_10 = Constants.START_YEAR - 10;

    @BeforeEach
    void setUp() {
        AssociationManager.getInstance().reset();
    }


    @Test
    @DisplayName("addingPlayersTest")
    void addingPlayersTest() {
        Club club = new Club(ELEMENT_NAME, ELEMENT_NAME, ELEMENT_NAME);
        Team teamMSEN = new Team(PlayerLevel.MSEN, ELEMENT_NAME, club);
        Team teamMU12 = new Team(PlayerLevel.MU12, ELEMENT_NAME, club);
        Team teamFSEN = new Team(PlayerLevel.FSEN, ELEMENT_NAME, club);
        Team teamFU12 = new Team(PlayerLevel.FU12, ELEMENT_NAME, club);

        PlayerInfo playerMSEN = PlayerFactory.getRandomPlayerInfo(PlayerGender.M, AGE_30, 1);
        PlayerInfo playerMJUN = PlayerFactory.getRandomPlayerInfo(PlayerGender.M, AGE_10, 2);
        PlayerInfo playerFSEN = PlayerFactory.getRandomPlayerInfo(PlayerGender.F, AGE_30, 3);
        PlayerInfo playerFJUN = PlayerFactory.getRandomPlayerInfo(PlayerGender.F, AGE_10, 4);

        Result result = teamMSEN.addPlayer(playerMSEN);
        assertTrue(result.ok(), "Senior M player should be added into senior M team");
        result = teamMSEN.addPlayer(playerMJUN);
        assertFalse(result.ok(), "Junior M player shouldn't be added into senior M team");
        result = teamMSEN.addPlayer(playerFSEN);
        assertFalse(result.ok(), "Senior F player shouldn't be added into senior M team");
        result = teamMSEN.addPlayer(playerFJUN);
        assertFalse(result.ok(), "Junior F player shouldn't be added into senior M team");

        result = teamMU12.addPlayer(playerMSEN);
        assertFalse(result.ok(), "Senior M player shouldn't be added into junior M team");
        result = teamMU12.addPlayer(playerMJUN);
        assertTrue(result.ok(), "Junior M player should be added into junior M team");
        result = teamMU12.addPlayer(playerFSEN);
        assertFalse(result.ok(), "Senior F player shouldn't be added into junior M team");
        result = teamMU12.addPlayer(playerFJUN);
        assertFalse(result.ok(), "Junior F player shouldn't be added into junior M team");

        result = teamFSEN.addPlayer(playerMSEN);
        assertFalse(result.ok(), "Senior M player shouldn't be added into senior F team");
        result = teamFSEN.addPlayer(playerMJUN);
        assertFalse(result.ok(), "Junior M player shouldn't be added into senior F team");
        result = teamFSEN.addPlayer(playerFSEN);
        assertTrue(result.ok(), "Senior F player should be added into senior F team");
        result = teamFSEN.addPlayer(playerFJUN);
        assertFalse(result.ok(), "Junior F player shouldn't be added into senior F team");

        result = teamFU12.addPlayer(playerMSEN);
        assertFalse(result.ok(), "Senior M player shouldn't be added into junior F team");
        result = teamFU12.addPlayer(playerMJUN);
        assertFalse(result.ok(), "Junior M player shouldn't be added into junior F team");
        result = teamFU12.addPlayer(playerFSEN);
        assertFalse(result.ok(), "Senior F player shouldn't be added into junior F team");
        result = teamFU12.addPlayer(playerFJUN);
        assertTrue(result.ok(), "Junior F player should be added into junior F team");
    }

}
