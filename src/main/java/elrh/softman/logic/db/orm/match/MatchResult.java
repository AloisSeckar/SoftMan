package elrh.softman.logic.db.orm.match;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.db.orm.TeamInfo;
import lombok.*;

@DatabaseTable(tableName = "softman_match_result")
@Data @NoArgsConstructor
public class MatchResult {
    
    @DatabaseField(generatedId = true)
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
        awayTeam = GameDBManager.getInstance().getTeam(source.getAwayLineup().getTeamId());
        homeTeam = GameDBManager.getInstance().getTeam(source.getHomeLineup().getTeamId());
        
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
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.matchId ^ (this.matchId >>> 32));
        return hash;
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
        final MatchResult other = (MatchResult) obj;
        return this.matchId == other.matchId;
    }
    
}
