package elrh.softman.logic.core;

import elrh.softman.db.orm.MatchInfo;
import elrh.softman.db.orm.MatchPlayByPlay;
import elrh.softman.logic.MatchSimulator;
import elrh.softman.logic.enums.MatchStatus;
import elrh.softman.logic.core.stats.*;
import elrh.softman.utils.ErrorUtils;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextArea;
import lombok.Data;

@Data
public class Match {

    private final MatchInfo matchInfo;

    private final Team awayTeam;

    private final Team homeTeam;

    private final BoxScore boxScore = new BoxScore();

    private List<MatchPlayByPlay> playByPlay = new ArrayList<>();

    public Match(MatchInfo matchInfo, Team awayTeam, Team homeTeam) {

        if (matchInfo != null) {
            this.matchInfo = matchInfo;
            matchInfo.setStatus(MatchStatus.SCHEDULED);
        } else {
            this.matchInfo = new MatchInfo();
            ErrorUtils.raise("Illegal 'Match' constructor call with NULL 'matchInfo'");
        }
        
        if (awayTeam != null) {
            this.awayTeam = awayTeam;
            this.matchInfo.setAwayTeamId(awayTeam.getId());
        } else {
            this.awayTeam = null;
            ErrorUtils.raise("Illegal 'Match' constructor call with NULL 'awayTeam'");
        }
        
        if (homeTeam != null) {
            this.homeTeam = homeTeam;
            this.matchInfo.setHomeTeamId(homeTeam.getId());
        } else {
            this.homeTeam = null;
            ErrorUtils.raise("Illegal 'Match' constructor call with NULL 'homeTeam'");
        }

    }
    
    public void simulate(TextArea target) {
        MatchSimulator sim = new MatchSimulator(this, target);
        sim.simulateMatch();
    }

    public void printPlayByPlay(TextArea target) {
        playByPlay.forEach(pbp -> target.appendText(pbp.getPlay()));
    }

    public boolean isScheduled() {
        return matchInfo.getStatus() == MatchStatus.SCHEDULED;
    }

    public boolean isActive() {
        return matchInfo.getStatus() == MatchStatus.ACTIVE;
    }

    public boolean isFinished() {
        return matchInfo.getStatus() == MatchStatus.FINISHED;
    }

    public boolean homeTeamFinishedBatting() {
        return isFinished() && !matchInfo.isHomeTeamFinishedBatting();
    }
    
}
