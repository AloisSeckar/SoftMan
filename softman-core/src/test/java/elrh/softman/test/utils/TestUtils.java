package elrh.softman.test.utils;

import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Lineup;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.db.orm.match.MatchInfo;

public class TestUtils {

    public static final String ELEMENT_NAME = "Test";

    public static Club getTestClub() {
        return new Club(ELEMENT_NAME, ELEMENT_NAME.substring(0, 3), ELEMENT_NAME, ELEMENT_NAME);
    }

    public static Match getTestMatch() {
        var matchId = 101L;
        var matchNumber = 101;
        var mockMatchInfo = new MatchInfo();
        mockMatchInfo.setMatchId(matchId);
        mockMatchInfo.setMatchNumber(matchNumber);
        mockMatchInfo.setStadium(ELEMENT_NAME);
        var mockAway = new Lineup(1, "a", "a", "a");
        var mockHome = new Lineup(1, "a", "a", "a");
        return new Match(mockMatchInfo, mockAway, mockHome);
    }

}
