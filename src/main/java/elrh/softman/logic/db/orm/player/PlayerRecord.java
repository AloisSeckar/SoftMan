package elrh.softman.logic.db.orm.player;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.AbstractDBEntity;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.enums.PlayerPosition;
import lombok.*;

@DatabaseTable(tableName = "softman_player_record")
@Data @EqualsAndHashCode(callSuper=true)
public class PlayerRecord extends AbstractDBEntity {

    @DatabaseField(generatedId = true)
    private long playerRecordId;

    @DatabaseField(canBeNull = false, foreign = true)
    private final PlayerInfo player;

    @DatabaseField(canBeNull = false)
    private final PlayerPosition position;

    public PlayerRecord(PlayerInfo player, PlayerPosition position) {
        this.player = player;
        this.position = position;
    }

    @DatabaseField(canBeNull = false, foreign = true)
    private PlayerStats stats = new PlayerStats();

    @Override
    public String toString() {
        return player.toString();
    }

    @Override
    public long getId() {
        return playerRecordId;
    }

    @Override
    public Result persist() {
        return GameDBManager.getInstance().saveObject(PlayerRecord.class, this);
    }
}
