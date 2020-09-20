package elrh.softman.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@DatabaseTable(tableName = "softman_teams")
@Data @NoArgsConstructor
public class TeamInfo {
    
    @DatabaseField(generatedId = true)
    private long teamId;
    
    @DatabaseField(canBeNull = false)
    private String teamName;
    
    @DatabaseField(canBeNull = false)
    private String gender;
    
    @DatabaseField(canBeNull = false)
    private String city;
    
    @DatabaseField(canBeNull = true)
    private String img;
    
    @DatabaseField(canBeNull = false)
    private int year;

    public TeamInfo(String teamName) {
        this.teamName = teamName;
        this.gender = "m";
        this.city = "City";
        this.year = 2020;
    }
    
    @Override
    public int hashCode() {
        return teamName != null ? teamName.hashCode() : 0;
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
        final TeamInfo other = (TeamInfo) obj;
        return (this.teamName == null ? other.teamName == null : this.teamName.equals(other.teamName));
    }
    
}
