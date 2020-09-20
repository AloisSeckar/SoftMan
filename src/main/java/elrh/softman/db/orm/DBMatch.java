package elrh.softman.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.Match;
import elrh.softman.logic.stats.BoxScore;

@DatabaseTable(tableName = "softman_matches")
public class DBMatch {
    
    @DatabaseField(generatedId = true)
    private long matchId;
 
    @DatabaseField(canBeNull = false)
    private String awayTeam;
    
    @DatabaseField(canBeNull = false)
    private String homeTeam;
    
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

    public DBMatch() {
    }
    
    public DBMatch(Match source) {
        awayTeam = source.getAwayTeam().getName();
        homeTeam = source.getHomeTeam().getName();
        
        BoxScore boxScore = source.getBoxScore();
        innings = boxScore.getInnings();
        awayRuns = boxScore.getPoints(true);
        homeRuns = boxScore.getPoints(false);
        awayHits = boxScore.getHits(true);
        homeHits = boxScore.getHits(false);
        awayErrors = boxScore.getErrors(true);
        homeErrors = boxScore.getErrors(false);
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public int getInnings() {
        return innings;
    }

    public void setInnings(int innings) {
        this.innings = innings;
    }

    public int getAwayRuns() {
        return awayRuns;
    }

    public void setAwayRuns(int awayRuns) {
        this.awayRuns = awayRuns;
    }

    public int getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(int homeRuns) {
        this.homeRuns = homeRuns;
    }

    public int getAwayHits() {
        return awayHits;
    }

    public void setAwayHits(int awayHits) {
        this.awayHits = awayHits;
    }

    public int getHomeHits() {
        return homeHits;
    }

    public void setHomeHits(int homeHits) {
        this.homeHits = homeHits;
    }

    public int getAwayErrors() {
        return awayErrors;
    }

    public void setAwayErrors(int awayErrors) {
        this.awayErrors = awayErrors;
    }

    public int getHomeErrors() {
        return homeErrors;
    }

    public void setHomeErrors(int homeErrors) {
        this.homeErrors = homeErrors;
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
        final DBMatch other = (DBMatch) obj;
        return this.matchId == other.matchId;
    }
    
}
