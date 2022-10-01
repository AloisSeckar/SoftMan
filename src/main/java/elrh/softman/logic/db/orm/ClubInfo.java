package elrh.softman.logic.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@DatabaseTable(tableName = "softman_club_info")
@Data @NoArgsConstructor
public class ClubInfo {

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

}
