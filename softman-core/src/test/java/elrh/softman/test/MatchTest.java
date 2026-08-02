package elrh.softman.test;

import elrh.softman.logic.core.Match;
import elrh.softman.logic.db.orm.match.MatchPlayByPlay;
import elrh.softman.logic.enums.MatchStatus;
import elrh.softman.test.utils.TestUtils;
import java.util.Collections;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MatchTest extends AbstractDBTest {

    private Match match;

    @BeforeEach
    void setUp() {
        match = TestUtils.getTestMatch();
    }

    @Test
    @DisplayName("printPlayByPlayTest")
    void printPlayByPlayTest() {
        var testString = "This is a test play";

        var mockPBP = new MatchPlayByPlay();
        mockPBP.setPlay(testString);
        var mockPBPList = Collections.singletonList(mockPBP);
        match.setPlayByPlay(mockPBPList);

        Platform.startup(() -> {}); // TODO produces 'Unsupported JavaFX configuration' warning into console
        var textArea = new TextArea();
        match.printPlayByPlay(textArea);
        assertEquals(textArea.getText(), testString, "TestString should be printed in TextArea");
        Platform.exit();
    }

    @Test
    @DisplayName("matchStatusTest")
    void matchStatusTest() {
        assertTrue(match.isScheduled(), "Match should be initially SCHEDULED");
        assertFalse(match.isActive(), "Match shouldn't be initially ACTIVE");
        assertFalse(match.isFinished(), "Match shouldn't be initially FINISHED");

        match.getMatchInfo().setStatus(MatchStatus.ACTIVE);
        assertFalse(match.isScheduled(), "Match shouldn't be SCHEDULED now");
        assertTrue(match.isActive(), "Match should be ACTIVE now");
        assertFalse(match.isFinished(), "Match shouldn't be FINISHED yet");

        match.getMatchInfo().setStatus(MatchStatus.FINISHED);
        assertFalse(match.isScheduled(), "Match shouldn't be SCHEDULED now");
        assertFalse(match.isActive(), "Match shouldn't be ACTIVE now");
        assertTrue(match.isFinished(), "Match should be FINISHED now");
    }

}
