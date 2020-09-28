package elrh.softman.logic;

import elrh.softman.db.orm.PlayerInfo;

public class LineupPosition {
    
    private PlayerInfo player;
    private Position position;

    public LineupPosition(PlayerInfo player, Position position) {
        this.player = player;
        this.position = position;
    }

    public PlayerInfo getPlayer() {
        return player;
    }

    public void setPlayer(PlayerInfo player) {
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
