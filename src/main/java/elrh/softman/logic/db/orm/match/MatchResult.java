package elrh.softman.logic.db.orm.match;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.Result;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.db.AbstractDBEntity;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.db.orm.TeamInfo;
import lombok.*;

@DatabaseTable(tableName = "softman_match_result")
@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
public class MatchResult extends AbstractDBEntity {

    @DatabaseField(generatedId = true)
    private long matchResultId;

    @DatabaseField(canBeNull = false)
    private long matchId;
 
    @DatabaseField(canBeNull = false, foreign=true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private TeamInfo awayTeam;
    
    @DatabaseField(canBeNull = false, foreign=true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private TeamInfo homeTeam;
    
    @DatabaseField(canBeNull = false)
    private int innings;
    
    @DatabaseField(canBeNull = false)
    private int awayRuns;
    
    @DatabaseField(canBeNull = false)
    private int homeRuns;
    
    @DatabaseField(canBeNull = false)
    private int awayHits;
    
    @DatabaseField(canBeNull = false)
    private int homeHits;
    
    @DatabaseField(canBeNull = false)
    private int awayErrors;
    
    @DatabaseField(canBeNull = false)
    private int homeErrors;
    
    public MatchResult(Match source) {
        awayTeam = (TeamInfo) GameDBManager.getInstance().getObjectById(TeamInfo.class, source.getAwayLineup().getLinuepInfo().getTeamId());
        homeTeam = (TeamInfo) GameDBManager.getInstance().getObjectById(TeamInfo.class, source.getHomeLineup().getLinuepInfo().getTeamId());
        
        var boxScore = source.getBoxScore();
        innings = boxScore.getInnings();
        awayRuns = boxScore.getTotalPoints(true);
        homeRuns = boxScore.getTotalPoints(false);
        awayHits = boxScore.getHits(true);
        homeHits = boxScore.getHits(false);
        awayErrors = boxScore.getErrors(true);
        homeErrors = boxScore.getErrors(false);
    }

    @Override
    public long getId() {
        return getMatchResultId();
    }

    @Override
    public Result persist() {
        return GameDBManager.getInstance().saveObject(MatchResult.class, this);
    }
    
}
