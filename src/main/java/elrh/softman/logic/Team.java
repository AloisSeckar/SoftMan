package elrh.softman.logic;

public class Team {
    
    private final String name;
    
    private final LineupPosition[] battingOrder = new LineupPosition[10];
    private final Player[] substitutes = new Player[8];

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public LineupPosition getBatter(int order) {
        return battingOrder[order];
    }
    
    public Player getFielder(int order) {
        Player ret = null;
        
        LineupPosition pos = battingOrder[order];
        if (pos != null) {
            ret = pos.getPlayer();
        }
        
        return ret;
    }
    
    public Player getFielder(Position position) {
        Player ret = null;
        
        for (int i = 0; i < 10; i++) {
            LineupPosition pos = battingOrder[i];
            if (pos != null && pos.getPosition() == position) {
                ret = pos.getPlayer();
                break;
            }
        }
        
        return ret;
    }
    
    public void fillPosition(Player player, Position position, int order) {
        LineupPosition newPosition = new LineupPosition(player, position);
        battingOrder[order] = newPosition;
    }
    
    public void addSubtitute(Player player) {
        int maxSubstitutes = battingOrder[9] == null ? 8 : 7;
        for (int i = 0; i < maxSubstitutes; i++) {
            if (substitutes[i] == null) {
                substitutes[i] = player;
                break;
            }
        }
    }
    
}
