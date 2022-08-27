package elrh.softman.logic.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.enums.LeagueLevel;
import lombok.*;

@DatabaseTable(tableName = "softman_teams")
@Data @NoArgsConstructor @RequiredArgsConstructor
public class TeamInfo {
    
    @DatabaseField(generatedId = true)
    private long teamId;

    @DatabaseField(canBeNull = false)
    @NonNull
    private LeagueLevel level;

    @DatabaseField(canBeNull = false, foreign = true)
    @NonNull
    private ClubInfo clubInfo;

    @DatabaseField(foreign = true)
    private LeagueInfo leagueInfo;
    
}
