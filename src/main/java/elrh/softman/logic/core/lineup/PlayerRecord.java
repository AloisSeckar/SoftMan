package elrh.softman.logic.core.lineup;

import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.logic.core.stats.Stats;
import lombok.Data;

@Data
public class PlayerRecord {

    private final int order; // TODO remove - not needed anymore
    private final PlayerInfo player;
    private final PlayerPosition position;
    private final Stats stats = new Stats();

}