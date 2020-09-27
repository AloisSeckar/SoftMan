package elrh.softman.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@DatabaseTable(tableName = "softman_leagues")
@Data @NoArgsConstructor
public class LeagueInfo {
    
    @DatabaseField(generatedId = true)
    private long leagueId;
    
    @DatabaseField(canBeNull = false)
    private String leagueName;
    
    @DatabaseField(canBeNull = false)
    private String gender;
    
    @DatabaseField(canBeNull = false)
    private int year;
    
    @DatabaseField(canBeNull = false)
    private int round;
    
    public LeagueInfo(String leagueName) {
        this.leagueName = leagueName;
        this.gender = "m";
        this.year = 2020;
        this.round = 1;
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
        return (this.leagueName == null ? other.leagueName == null : this.leagueName.equals(other.leagueName));
    }

    public void setRoundPlayed() {
        this.round++;
    }
    
}
