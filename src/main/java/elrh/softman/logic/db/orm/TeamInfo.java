package elrh.softman.logic.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.enums.PlayerLevel;
import lombok.*;

@DatabaseTable(tableName = "softman_teams")
@Data @NoArgsConstructor @RequiredArgsConstructor
public class TeamInfo {
    
    @DatabaseField(generatedId = true)
    private long teamId;

    @DatabaseField(canBeNull = false)
    @NonNull
    private PlayerLevel level;

    @DatabaseField(canBeNull = false, foreign = true)
    @NonNull
    private ClubInfo clubInfo;

    @DatabaseField(foreign = true)
    private LeagueInfo leagueInfo;
    
}
