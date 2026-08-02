package elrh.softman.logic.core.stats;

import java.util.UUID;
import lombok.*;

@Data
public class Standing implements Comparable<Standing> {

    private UUID standingId = UUID.randomUUID();
    private UUID leagueId;
    private UUID teamId;
    private String team;
    
    private int games;
    private int wins;
    private int loses;
    private int runsFor;
    private int runsAgainst;

    public Standing(UUID leagueId, UUID teamId, String team) {
        this.leagueId = leagueId;
        this.teamId = teamId;
        this.team = team;
    }

    public Standing() {
    }
    
    public int getPoints() {
        return this.wins * 2 + this.loses;
    }

    @Override
    public int compareTo(Standing other) {
        int ret;
        
        if (other != null) {
            ret = this.getPoints() - other.getPoints();
            if (ret == 0) {
                ret = this.games - other.getGames();
                if (ret == 0) {
                    int thisRunDifference = runsFor - runsAgainst;
                    int otherRunDifference = other.getRunsFor() - other.getRunsAgainst();
                    ret = thisRunDifference - otherRunDifference;
                    if (ret == 0) {
                        ret = this.runsFor - other.getRunsFor();
                        if (ret == 0) {
                        ret = this.runsAgainst - other.getRunsAgainst();
                        }
                    }
                }
            }
        } else {
            ret = 1;
        }
        
        return -ret;
    }
    
}
