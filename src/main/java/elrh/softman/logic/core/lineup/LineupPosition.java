package elrh.softman.logic.core.lineup;

import elrh.softman.db.orm.PlayerInfo;
import elrh.softman.logic.enums.Position;
import elrh.softman.logic.core.stats.Stats;
import lombok.Data;

@Data
public class LineupPosition {

    private final int order;
    private final PlayerInfo player;
    private final Position position;
    private final Stats stats = new Stats();

}
