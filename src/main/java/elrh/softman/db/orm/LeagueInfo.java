package elrh.softman.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.constants.Constants;
import elrh.softman.logic.enums.LeagueLevelEnum;
import java.util.Objects;
import lombok.*;

@DatabaseTable(tableName = "softman_leagues")
@Data @NoArgsConstructor
public class LeagueInfo {
    
    @DatabaseField(generatedId = true)
    private long leagueId;
    
    @DatabaseField(canBeNull = false)
    private String leagueName;
    
    @DatabaseField(canBeNull = false)
    private LeagueLevelEnum level;
    
    @DatabaseField(canBeNull = false)
    private int year;
    
    @DatabaseField(canBeNull = false)
    private int matchId;
    
    public LeagueInfo(String leagueName, LeagueLevelEnum level) {
        this.leagueName = leagueName;
        this.level = level;
        this.year = Constants.START_YEAR;
        this.matchId = 1000;
    }
    
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
