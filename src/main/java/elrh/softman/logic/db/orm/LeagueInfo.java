package elrh.softman.logic.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.enums.PlayerLevel;
import java.util.Objects;
import lombok.*;

@DatabaseTable(tableName = "softman_league_info")
@Data @NoArgsConstructor @RequiredArgsConstructor
public class LeagueInfo {
    
    @DatabaseField(generatedId = true)
    private long leagueId;
    
    @DatabaseField(canBeNull = false)
    @NonNull
    private String leagueName;
    
    @DatabaseField(canBeNull = false)
    @NonNull
    private PlayerLevel level;
    
    @DatabaseField(canBeNull = false)
    @NonNull
    private int year;
    
    @DatabaseField(canBeNull = false)
    @NonNull
    private int matchId;
    
    @Override
    public int hashCode() {
        return leagueName != null ? leagueName.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LeagueInfo other = (LeagueInfo) obj;
        return (Objects.equals(this.leagueName, other.leagueName));
    }
    
}
