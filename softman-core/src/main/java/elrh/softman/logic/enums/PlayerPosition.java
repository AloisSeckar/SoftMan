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
        
        ret.addAll(List.of(
            PITCHER, CATCHER, FIRST_BASE, SECOND_BASE, THIRD_BASE,
            SHORT_STOP, LEFT_FIELD, CENTER_FIELD, RIGHT_FIELD
        ));

        if (includeDP) {
            ret.add(DESIGNATED_PLAYER);
        }

        if (includeInGame) {
            ret.addAll(List.of(OFFENSIVE_ONLY, PINCH_HITTER, PINCH_RUNNER));
        }

        return ret;
    }
}
