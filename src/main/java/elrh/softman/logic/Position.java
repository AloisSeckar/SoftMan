package elrh.softman.logic;

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
    DESIGNATED_HITTER("DH"),
    OFFENSIVE_ONLY("OPO");
    
    private final String pos;
    
    private Position(String pos) {
        this.pos = pos;
    }
    
    @Override
    public String toString() {
        return pos;
    }
}
