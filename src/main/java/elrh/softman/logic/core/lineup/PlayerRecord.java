package elrh.softman.logic.core.lineup;

import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.db.orm.records.StatsRecord;
import elrh.softman.logic.enums.PlayerPosition;
import lombok.Data;

@Data
public class PlayerRecord {

    private final PlayerInfo player;
    private final PlayerPosition position;
    private StatsRecord stats = new StatsRecord();

    @Override
    public String toString() {
        return player.toString();
    }

}
