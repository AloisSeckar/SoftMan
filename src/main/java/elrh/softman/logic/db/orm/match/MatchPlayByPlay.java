package elrh.softman.logic.db.orm.match;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.AbstractDBEntity;
import elrh.softman.logic.db.GameDBManager;
import lombok.*;

@DatabaseTable(tableName = "softman_match_pbp")
@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
public class MatchPlayByPlay extends AbstractDBEntity {

    @DatabaseField(generatedId = true)
    private long matchPlayByPlayId;

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

    @Override
    public long getId() {
        return getMatchPlayByPlayId();
    }

    @Override
    public Result persist() {
        return GameDBManager.getInstance().saveObject(MatchPlayByPlay.class, this);
    }

}
