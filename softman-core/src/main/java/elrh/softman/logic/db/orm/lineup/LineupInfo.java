package elrh.softman.logic.db.orm.lineup;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.AbstractDBEntity;
import elrh.softman.logic.db.GameDBManager;
import lombok.*;

@DatabaseTable(tableName = "softman_lineup_info")
@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class LineupInfo extends AbstractDBEntity {

    @DatabaseField(generatedId = true)
    private long lineupId;

    @DatabaseField(canBeNull = false)
    private final long teamId;

    @DatabaseField(canBeNull = false)
    @NonNull
    private String teamName;

    @DatabaseField(canBeNull = false)
    @NonNull
    private String teamShortName;

    @DatabaseField(canBeNull = false)
    @NonNull
    private String teamLogo;

    @Override
    public long getId() {
        return lineupId;
    }

    @Override
    public Result persist() {
        return GameDBManager.getInstance().saveObject(LineupInfo.class, this);
    }

}
