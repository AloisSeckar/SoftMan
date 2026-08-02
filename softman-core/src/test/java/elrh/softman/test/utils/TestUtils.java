package elrh.softman.test.utils;

import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Lineup;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.core.data.MatchInfo;
import java.util.UUID;

public class TestUtils {

    public static final String ELEMENT_NAME = "Test";

    public static Club getTestClub() {
        return new Club(ELEMENT_NAME, ELEMENT_NAME.substring(0, 3), ELEMENT_NAME, ELEMENT_NAME);
    }

    public static Match getTestMatch() {
        var matchNumber = 101;
        var mockMatchInfo = new MatchInfo();
        mockMatchInfo.setMatchNumber(matchNumber);
        mockMatchInfo.setStadium(ELEMENT_NAME);
        var teamId = UUID.randomUUID();
        var mockAway = new Lineup(teamId, "a", "a", "a");
        var mockHome = new Lineup(teamId, "a", "a", "a");
        return new Match(mockMatchInfo, mockAway, mockHome);
    }

}
