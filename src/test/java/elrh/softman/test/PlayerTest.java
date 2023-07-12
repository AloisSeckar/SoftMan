package elrh.softman.test;

import elrh.softman.logic.core.Player;
import elrh.softman.logic.enums.ActivityType;
import elrh.softman.logic.enums.PlayerGender;
import elrh.softman.utils.factory.PlayerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;

public class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = PlayerFactory.getRandomPlayer(PlayerGender.M, 2000, 1);
    }

    @Test
    @DisplayName("increasePlayerFatigueTest")
    void increasePlayerFatigueTest() {
        int fatigue = player.getPlayerInfo().getAttributes().getFatigue();
        int endurance = player.getPlayerInfo().getAttributes().getEndurance();
        int modifier = endurance / 10;

        // + (20 - modifier)
        player.increaseFatigue(ActivityType.TRAINING);
        // + (15 - modifier)
        player.increaseFatigue(ActivityType.MATCH);

        int expectedFatigue = fatigue + (20 - modifier) + (15 - modifier);
        int actualFatigue = player.getPlayerInfo().getAttributes().getFatigue();

        assertEquals(expectedFatigue, actualFatigue,"Player's fatigue expected to be " + expectedFatigue + ", but was " + actualFatigue);

        player.getPlayerInfo().getAttributes().setFatigue(99);
        player.increaseFatigue(ActivityType.MATCH);
        actualFatigue = player.getPlayerInfo().getAttributes().getFatigue();

        assertEquals(100, actualFatigue, "Player's max fatigue is limited to 100");
    }

    @Test
    @DisplayName("decreasePlayerFatigueTest")
    void decreasePlayerFatigueTest() {
        player.getPlayerInfo().getAttributes().setFatigue(100);

        int recovery = player.getPlayerInfo().getAttributes().getRecovery();
        int modifier = recovery / 10;

        // - (10 + modifier)
        player.decreaseFatigue();
        // - (10 + modifier)
        player.decreaseFatigue();

        int expectedFatigue = 100 - (10 + modifier) - (10 + modifier);
        int actualFatigue = player.getPlayerInfo().getAttributes().getFatigue();

        assertEquals(expectedFatigue, actualFatigue, "Player's fatigue expected to be " + expectedFatigue + ", but was " + actualFatigue);

        player.getPlayerInfo().getAttributes().setFatigue(1);
        player.decreaseFatigue();
        actualFatigue = player.getPlayerInfo().getAttributes().getFatigue();

        assertEquals(0, actualFatigue, "Player's max fatigue is limited to 100");
    }

}
