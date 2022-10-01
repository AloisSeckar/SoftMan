package elrh.softman.logic.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.AbstractDBEntity;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.db.orm.player.PlayerAttributes;
import lombok.*;

@DatabaseTable(tableName = "softman_club_info")
@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
public class ClubInfo extends AbstractDBEntity {

    @DatabaseField(generatedId = true)
    private long clubId;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String logo;

    @DatabaseField(canBeNull = false)
    private String city;

    @DatabaseField(canBeNull = false)
    private String stadium;

    @DatabaseField(canBeNull = false)
    private int registered;

    @DatabaseField(canBeNull = false)
    private long money;

    @Override
    public long getId() {
        return getClubId();
    }

    @Override
    public Result persist() {
        return GameDBManager.getInstance().saveObject(ClubInfo.class, this);
    }

}
