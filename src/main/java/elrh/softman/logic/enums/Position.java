package elrh.softman.logic.enums;

import java.util.Arrays;
import java.util.List;

public enum Position {
    
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
    OFFENSIVE_ONLY("OPO");
    
    private final String pos;
    
    Position(String pos) {
        this.pos = pos;
    }
    
    @Override
    public String toString() {
        return pos;
    }

    public static List<Position> getAvailablePositions() {
        // TODO allow playing with DP/FLEX
        return Arrays.asList(PITCHER, CATCHER, FIRST_BASE, SECOND_BASE, THIRD_BASE, SHORT_STOP, LEFT_FIELD, CENTER_FIELD, RIGHT_FIELD);
    }
}
