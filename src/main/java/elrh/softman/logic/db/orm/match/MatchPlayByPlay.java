package elrh.softman.logic.db.orm.match;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@DatabaseTable(tableName = "softman_match_pbp")
@Data @NoArgsConstructor
public class MatchPlayByPlay {

    @DatabaseField(generatedId = true)
    private long pbpId;

    @DatabaseField
    private long matchId;

    @DatabaseField
    private int ord;

    @DatabaseField
    private String play;

    public MatchPlayByPlay(long matchId, int ord, String play) {
        this.matchId = matchId;
        this.ord = ord;
        this.play = play;
    }

}
