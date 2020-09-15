package elrh.softman.logic;

public class LineupPosition {
    
    private Player player;
    private Position position;

    public LineupPosition(Player player, Position position) {
        this.player = player;
        this.position = position;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return player.toString() + " [" + position + "]";
    }
    
    
    
}
