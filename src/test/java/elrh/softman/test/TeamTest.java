package elrh.softman.test;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.*;
import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.enums.PlayerGender;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.utils.Constants;
import elrh.softman.utils.factory.PlayerFactory;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    private static final String ELEMENT_NAME = "Test";

    @BeforeEach
    void setUp() {
        AssociationManager.getInstance().reset();
    }


    @Test
    @DisplayName("addingPlayersTest")
    void addingPlayersTest() {
        Club club = new Club(ELEMENT_NAME, ELEMENT_NAME, ELEMENT_NAME);
        Team teamMSEN = new Team(PlayerLevel.MSEN, club);
        Team teamMU12 = new Team(PlayerLevel.MU12, club);
        Team teamFSEN = new Team(PlayerLevel.FSEN, club);
        Team teamFU12 = new Team(PlayerLevel.FU12, club);

        PlayerInfo playerMSEN = PlayerFactory.getRandomPlayerInfo(1);
        playerMSEN.setGender(PlayerGender.M);
        playerMSEN.setBirth(Constants.START_YEAR - 30);

        PlayerInfo playerMJUN = PlayerFactory.getRandomPlayerInfo(2);
        playerMJUN.setGender(PlayerGender.M);
        playerMJUN.setBirth(Constants.START_YEAR - 10);

        PlayerInfo playerFSEN = PlayerFactory.getRandomPlayerInfo(3);
        playerFSEN.setGender(PlayerGender.F);
        playerFSEN.setBirth(Constants.START_YEAR - 30);

        PlayerInfo playerFJUN = PlayerFactory.getRandomPlayerInfo(4);
        playerFJUN.setGender(PlayerGender.F);
        playerFJUN.setBirth(Constants.START_YEAR - 10);

        boolean addPlayer;

        addPlayer = teamMSEN.addPlayer(playerMSEN);
        assertTrue(addPlayer, "Senior M player shall be added into senior M team");
        addPlayer = teamMSEN.addPlayer(playerMJUN);
        assertFalse(addPlayer, "Junior M player shall not be added into senior M team");
        addPlayer = teamMSEN.addPlayer(playerFSEN);
        assertFalse(addPlayer, "Senior F player shall not be added into senior M team");
        addPlayer = teamMSEN.addPlayer(playerFJUN);
        assertFalse(addPlayer, "Junior F player shall not be added into senior M team");

        addPlayer = teamMU12.addPlayer(playerMSEN);
        assertFalse(addPlayer, "Senior M player shall not be added into junior M team");
        addPlayer = teamMU12.addPlayer(playerMJUN);
        assertTrue(addPlayer, "Junior M player shall be added into junior M team");
        addPlayer = teamMU12.addPlayer(playerFSEN);
        assertFalse(addPlayer, "Senior F player shall not be added into junior M team");
        addPlayer = teamMU12.addPlayer(playerFJUN);
        assertFalse(addPlayer, "Junior F player shall not be added into junior M team");

        addPlayer = teamFSEN.addPlayer(playerMSEN);
        assertFalse(addPlayer, "Senior M player shall not be added into senior F team");
        addPlayer = teamFSEN.addPlayer(playerMJUN);
        assertFalse(addPlayer, "Junior M player shall not be added into senior F team");
        addPlayer = teamFSEN.addPlayer(playerFSEN);
        assertTrue(addPlayer, "Senior F player shall be added into senior F team");
        addPlayer = teamFSEN.addPlayer(playerFJUN);
        assertFalse(addPlayer, "Junior F player shall not be added into senior F team");

        addPlayer = teamFU12.addPlayer(playerMSEN);
        assertFalse(addPlayer, "Senior M player shall not be added into junior F team");
        addPlayer = teamFU12.addPlayer(playerMJUN);
        assertFalse(addPlayer, "Junior M player shall not be added into junior F team");
        addPlayer = teamFU12.addPlayer(playerFSEN);
        assertFalse(addPlayer, "Senior F player shall not be added into junior F team");
        addPlayer = teamFU12.addPlayer(playerFJUN);
        assertTrue(addPlayer, "Junior F player shall be added into junior F team");
    }

}
