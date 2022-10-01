package elrh.softman.logic.core.lineup;

import elrh.softman.logic.db.orm.player.PlayerInfo;
import elrh.softman.logic.db.orm.player.PlayerStats;
import elrh.softman.logic.enums.PlayerPosition;
import lombok.Data;

@Data
public class PlayerRecord {

    private final PlayerInfo player;
    private final PlayerPosition position;
    private PlayerStats stats = new PlayerStats();

    @Override
    public String toString() {
        return player.toString();
    }

}
