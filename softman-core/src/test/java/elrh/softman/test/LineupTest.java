package elrh.softman.test;

import elrh.softman.logic.Result;
import elrh.softman.logic.core.Lineup;
import elrh.softman.logic.core.data.PlayerInfo;
import elrh.softman.logic.core.data.PlayerRecord;
import elrh.softman.logic.enums.PlayerGender;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.utils.factory.PlayerFactory;
import static elrh.softman.test.utils.TestUtils.ELEMENT_NAME;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

public class LineupTest {

    private final PlayerInfo player1 = PlayerFactory.getRandomPlayer(PlayerGender.M, 2022, 1).getPlayerInfo();
    private final PlayerInfo player2 = PlayerFactory.getRandomPlayer(PlayerGender.M, 2022, 2).getPlayerInfo();

    private final PlayerRecord playerRecord1 = new PlayerRecord(player1, PlayerPosition.PITCHER);
    private final PlayerRecord playerRecord2 = new PlayerRecord(player2, PlayerPosition.CATCHER);

    private Lineup lineup;
    private Result result;

    @BeforeEach
    void setUp() {
        lineup = new Lineup(java.util.UUID.randomUUID(), ELEMENT_NAME, "E", ELEMENT_NAME);
    }

    @Test
    @DisplayName("initPositionPlayerTest")
    void initPositionPlayerTest() {
        result = lineup.initPositionPlayer(1, playerRecord1);
        assertTrue(result.ok(), "init player 1 should be successful");

        var retrieved = lineup.getPositionPlayers()[0].get(0);
        assertEquals(1, retrieved.getPlayer().getNumber(), "player should be #1");
        assertEquals(PlayerPosition.PITCHER, retrieved.getPosition(), "position should be P");

        result = lineup.initPositionPlayer(0, playerRecord2);
        assertFalse(result.ok(), "init player 2 shouldn't be successful - out of bounds");
        assertThat(result.message(), containsString("out of bounds"));

        result = lineup.initPositionPlayer(100, playerRecord2);
        assertFalse(result.ok(), "init player 2 shouldn't be successful - out of bounds");
        assertThat(result.message(), containsString("out of bounds"));
    }

    @Test
    @DisplayName("initSubstituteTest")
    void initSubstituteTest() {
        result = lineup.initSubstitute(1, playerRecord2);
        assertTrue(result.ok(), "init sub 1 should be successful");

        var retrieved = lineup.getSubstitutes()[0];
        assertEquals(2, retrieved.getPlayer().getNumber(), "sub 1 should be #2");
        assertEquals(PlayerPosition.CATCHER, retrieved.getPosition(), "position should be C");

        result = lineup.initSubstitute(0, playerRecord1);
        assertFalse(result.ok(), "init sub 2 shouldn't be successful - out of bounds");
        assertThat(result.message(), containsString("out of bounds"));

        result = lineup.initSubstitute(100, playerRecord1);
        assertFalse(result.ok(), "init sub 2 shouldn't be successful - out of bounds");
        assertThat(result.message(), containsString("out of bounds"));
    }

    @Test
    @DisplayName("substitutePlayerTest")
    void substitutePlayerTest() {
        result = lineup.initPositionPlayer(1, playerRecord1);
        result = lineup.substitutePlayer(1, playerRecord2);
        assertTrue(result.ok(), "substitution 1 should be successful");

        var retrieved = lineup.getPositionPlayers()[0].get(1);
        assertEquals(2, retrieved.getPlayer().getNumber(), "player should be #2");
        assertEquals(PlayerPosition.CATCHER, retrieved.getPosition(), "position should be C");

        result = lineup.substitutePlayer(2, playerRecord1);
        assertFalse(result.ok(), "substitution 2 shouldn't be successful - not initialized");
        assertThat(result.message(), containsString("not initialized"));

        result = lineup.substitutePlayer(0, playerRecord1);
        assertFalse(result.ok(), "substitution 2 shouldn't be successful - out of bounds");
        assertThat(result.message(), containsString("out of bounds"));

        result = lineup.substitutePlayer(100, playerRecord1);
        assertFalse(result.ok(), "substitution 2 shouldn't be successful - out of bounds");
        assertThat(result.message(), containsString("out of bounds"));
    }

    @Test
    @DisplayName("getCurrentBatterTest")
    void getCurrentBatterTest() {
        var retrieved = lineup.getCurrentBatter(1);
        assertNull(retrieved, "no batter[1] should be set yet");

        lineup.initPositionPlayer(1, playerRecord1);
        retrieved = lineup.getCurrentBatter(1);
        assertNotNull(retrieved, "batter[1] should be set now");
        assertEquals(1, retrieved.getPlayer().getNumber(), "player should be #1");
        assertEquals(PlayerPosition.PITCHER, retrieved.getPosition(), "position should be P");

        result = lineup.substitutePlayer(1, playerRecord2);
        retrieved = lineup.getCurrentBatter(1);
        assertNotNull(retrieved, "batter[1] should be set now");
        assertEquals(2, retrieved.getPlayer().getNumber(), "player should be #2");
        assertEquals(PlayerPosition.CATCHER, retrieved.getPosition(), "position should be C");
    }

    @Test
    @DisplayName("getCurrentPositionPlayerTest")
    void getCurrentPositionPlayerTest() {
        var retrieved = lineup.getCurrentPositionPlayer(PlayerPosition.PITCHER);
        assertNull(retrieved, "no pitcher should be set yet");

        lineup.initPositionPlayer(1, playerRecord1);
        retrieved = lineup.getCurrentPositionPlayer(PlayerPosition.PITCHER);
        assertNotNull(retrieved, "pitcher should be set now");
        assertEquals(1, retrieved.getPlayer().getNumber(), "player should be #1");
        assertEquals(PlayerPosition.PITCHER, retrieved.getPosition(), "position should be P");

        result = lineup.substitutePlayer(1, playerRecord2);
        retrieved = lineup.getCurrentPositionPlayer(PlayerPosition.CATCHER);
        assertNotNull(retrieved, "catcher should be set now");
        assertEquals(2, retrieved.getPlayer().getNumber(), "player should be #2");
        assertEquals(PlayerPosition.CATCHER, retrieved.getPosition(), "position should be C");

        retrieved = lineup.getCurrentPositionPlayer(PlayerPosition.PITCHER);
        assertNull(retrieved, "no pitcher should be set now");
    }

    @Test
    @DisplayName("getCurrentFieldersTest")
    void getCurrentFieldersTest() {
        lineup.initPositionPlayer(1, playerRecord1);
        lineup.initPositionPlayer(2, playerRecord2);

        var fielders = lineup.getCurrentFielders();
        assertNotNull(fielders[0], "pitcher should be set");
        assertEquals(1, fielders[0].getPlayer().getNumber(), "pitcher should be #1");
        assertNotNull(fielders[1], "catcher should be set");
        assertEquals(2, fielders[1].getPlayer().getNumber(), "catcher should be #2");
        assertNull(fielders[2], "first base shouldn't be set");
    }

    @Test
    @DisplayName("useDPTest")
    void useDPTest() {
        assertFalse(lineup.useDP(), "DP shouldn't be set");
        result = lineup.initPositionPlayer(10, playerRecord1);
        assertTrue(lineup.useDP(), "DP shouldn't be set");
    }

    private PlayerInfo[] fillDefense() {
        var positions = PlayerPosition.getAvailablePositions(false, false);
        var starters = new PlayerInfo[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            starters[i] = PlayerFactory.getRandomPlayer(PlayerGender.M, 2022, i + 10).getPlayerInfo();
            lineup.initPositionPlayer(i + 1, new PlayerRecord(starters[i], positions.get(i)));
        }
        return starters;
    }

    @Test
    @DisplayName("replacementRulesTest")
    void replacementRulesTest() {
        var starters = fillDefense();
        lineup.initSubstitute(1, playerRecord1);

        assertEquals(1, lineup.getAvailableReplacements(1).size(), "only the bench player should be available");

        assertTrue(lineup.replacePlayer(1, player1, PlayerPosition.PINCH_HITTER).ok(), "sub should enter");
        assertFalse(lineup.replacePlayer(2, player1, PlayerPosition.PINCH_HITTER).ok(), "sub cannot enter twice");

        var available = lineup.getAvailableReplacements(1);
        assertEquals(1, available.size(), "only the starter re-entry should be available");
        assertEquals(starters[0], available.get(0), "starter should be able to re-enter");

        assertTrue(lineup.replacePlayer(1, starters[0], PlayerPosition.PITCHER).ok(), "starter should re-enter");
        assertTrue(lineup.getAvailableReplacements(1).isEmpty(), "removed sub and re-entered starter cannot come back");
        assertSame(lineup.getPositionPlayers()[0].get(0).getStats(), lineup.getCurrentBatter(1).getStats(),
            "re-entered starter keeps one stats line");
    }

    @Test
    @DisplayName("defenseTest")
    void defenseTest() {
        fillDefense();
        lineup.initSubstitute(1, playerRecord1);
        assertTrue(lineup.checkDefense().ok(), "full defense should be valid");

        lineup.replacePlayer(6, player1, PlayerPosition.PINCH_RUNNER);
        assertFalse(lineup.checkDefense().ok(), "PR must get a defensive position");

        var settled = lineup.settleDefense();
        assertEquals(1, settled.size(), "only the PR should be settled");
        assertEquals(PlayerPosition.SHORT_STOP, lineup.getCurrentBatter(6).getPosition(), "PR should take the open position");
        assertTrue(lineup.checkDefense().ok(), "settled defense should be valid");

        assertTrue(lineup.changePosition(1, PlayerPosition.CATCHER).ok(), "position change should be accepted");
        assertFalse(lineup.checkDefense().ok(), "two catchers and no pitcher is invalid");
        assertTrue(lineup.changePosition(2, PlayerPosition.PITCHER).ok(), "position change should be accepted");
        assertTrue(lineup.checkDefense().ok(), "swapped defense should be valid");
        assertEquals(PlayerPosition.PITCHER, lineup.getPositionPlayers()[0].get(0).getPosition(), "starter record stays untouched");
    }

}
