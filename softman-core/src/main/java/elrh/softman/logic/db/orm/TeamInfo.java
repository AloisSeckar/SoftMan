package elrh.softman.logic.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.AbstractDBEntity;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.enums.PlayerLevel;
import lombok.*;

@DatabaseTable(tableName = "softman_team_info")
@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
@RequiredArgsConstructor // TODO why RequiredArgsConstructor not working out of the box with Data?
public class TeamInfo extends AbstractDBEntity {
    
    @DatabaseField(generatedId = true)
    private long teamId;

    @DatabaseField(canBeNull = false)
    @NonNull
    private PlayerLevel level;

    @DatabaseField(canBeNull = false)
    @NonNull
    private String name;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    @NonNull
    private ClubInfo clubInfo;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private LeagueInfo leagueInfo;

    @Override
    public long getId() {
        return getTeamId();
    }

    @Override
    public Result persist() {
        return GameDBManager.getInstance().saveObject(TeamInfo.class, this);
    }
    
}
