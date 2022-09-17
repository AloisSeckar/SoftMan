package elrh.softman.logic.enums;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PlayerPosition {
    
    PITCHER("P"),
    CATCHER("C"),
    FIRST_BASE("1B"),
    SECOND_BASE("2B"),
    THIRD_BASE("3B"),
    SHORT_STOP("SS"),
    LEFT_FIELD("LF"),
    CENTER_FIELD("CF"),
    RIGHT_FIELD("RF"),
    DESIGNATED_PLAYER("DP"),
    OFFENSIVE_ONLY("OPO"),
    PINCH_HITTER("PH"),
    PINCH_RUNNER("PR");
    
    private final String pos;
    
    @Override
    public String toString() {
        return pos;
    }

    public static List<PlayerPosition> getAvailablePositions(boolean includeDP, boolean includeInGame) {
        var ret = new ArrayList<PlayerPosition>();

        ret.add(PITCHER);
        ret.add(CATCHER);
        ret.add(FIRST_BASE);
        ret.add(SECOND_BASE);
        ret.add(THIRD_BASE);
        ret.add(SHORT_STOP);
        ret.add(LEFT_FIELD);
        ret.add(CENTER_FIELD);
        ret.add(RIGHT_FIELD);

        if (includeDP) {
            ret.add(DESIGNATED_PLAYER);
        }

        if (includeInGame) {
            ret.add(OFFENSIVE_ONLY);
            ret.add(PINCH_HITTER);
            ret.add(PINCH_RUNNER);
        }

        return ret;
    }
}
