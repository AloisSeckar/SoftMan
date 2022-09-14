package elrh.softman.test;

import elrh.softman.logic.Result;
import elrh.softman.logic.core.lineup.*;
import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.enums.PlayerGender;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.utils.factory.PlayerFactory;
import static elrh.softman.test.utils.TestUtils.ELEMENT_NAME;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class LineupTest {

    private final PlayerInfo player1 = PlayerFactory.getRandomPlayerInfo(PlayerGender.M, 2022, 1);
    private final PlayerInfo player2 = PlayerFactory.getRandomPlayerInfo(PlayerGender.M, 2022, 2);

    private final PlayerRecord playerRecord1 = new PlayerRecord(1, player1, PlayerPosition.PITCHER);
    private final PlayerRecord playerRecord2 = new PlayerRecord(2, player2, PlayerPosition.CATCHER);

    private Lineup lineup;
    private Result result;

    @BeforeEach
    void setUp() {
        lineup = new Lineup(1, ELEMENT_NAME);
    }

    @Test
    @DisplayName("initPositionPlayeTest")
    void initPositionPlayerTest() {
        result = lineup.initPositionPlayer(1, playerRecord1);
        assertTrue(result.ok(), "init player 1 should be successful");
        PlayerRecord retrieved = lineup.getPositionPlayers()[0].get(0);
        assertEquals(1, retrieved.getPlayer().getNumber(), "player should be #1");
        assertEquals(PlayerPosition.PITCHER, retrieved.getPosition(), "position should be P");
        result = lineup.initPositionPlayer(0, playerRecord2);
        assertFalse(result.ok(), "init player 2 shouldn't be possible");
        result = lineup.initPositionPlayer(100, playerRecord2);
        assertFalse(result.ok(), "init player 2 shouldn't be possible");
    }

    @Test
    @DisplayName("initSubstituteTest")
    void initSubstituteTest() {
        result = lineup.initSubstitute(1, playerRecord2);
        assertTrue(result.ok(), "init sub 1 should be successful");
        PlayerRecord retrieved = lineup.getSubstitutes()[0];
        assertEquals(2, retrieved.getPlayer().getNumber(), "sub 1 should be #2");
        assertEquals(PlayerPosition.CATCHER, retrieved.getPosition(), "position should be C");
        result = lineup.initSubstitute(0, playerRecord1);
        assertFalse(result.ok(), "init sub 2 shouldn't be successful - out of bounds");
        result = lineup.initSubstitute(100, playerRecord1);
        assertFalse(result.ok(), "init sub 2 shouldn't be successful - out of bounds");
    }

    @Test
    @DisplayName("substitutePlayerTest")
    void substitutePlayerTest() {
        result = lineup.initPositionPlayer(1, playerRecord1);
        result = lineup.substitutePlayer(1, playerRecord2);
        assertTrue(result.ok(), "substitution 1 should be successful");
        PlayerRecord retrieved = lineup.getPositionPlayers()[0].get(1);
        assertEquals(2, retrieved.getPlayer().getNumber(), "player should be #2");
        assertEquals(PlayerPosition.CATCHER, retrieved.getPosition(), "position should be C");
        result = lineup.substitutePlayer(2, playerRecord1);
        assertFalse(result.ok(), "substitution 2 shouldn't be successful");
    }

    @Test
    @DisplayName("useDPTest")
    void useDPTest() {
        assertFalse(lineup.useDP(), "DP shouldn't be set");
        result = lineup.initPositionPlayer(10, playerRecord1);
        assertTrue(lineup.useDP(), "DP shouldn't be set");
    }

}
