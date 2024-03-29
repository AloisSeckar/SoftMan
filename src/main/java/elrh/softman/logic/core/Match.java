package elrh.softman.logic.core;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.db.orm.match.MatchInfo;
import elrh.softman.logic.db.orm.match.MatchPlayByPlay;
import elrh.softman.logic.enums.MatchStatus;
import elrh.softman.logic.core.stats.*;
import elrh.softman.utils.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.scene.control.TextArea;
import lombok.Data;

@Data
public class Match {

    private final MatchInfo matchInfo;

    private final Lineup awayLineup;

    private final Lineup homeLineup;

    private final BoxScore boxScore = new BoxScore();

    private List<MatchPlayByPlay> playByPlay = new ArrayList<>();

    public Match(MatchInfo matchInfo, Lineup awayLineup, Lineup homeLineup) {

        if (matchInfo != null) {
            this.matchInfo = matchInfo;
            matchInfo.setStatus(MatchStatus.SCHEDULED);
        } else {
            this.matchInfo = new MatchInfo();
            ErrorUtils.raise("Illegal 'Match' constructor call with NULL 'matchInfo'");
        }
        
        if (awayLineup != null) {
            this.awayLineup = awayLineup;
            this.matchInfo.setAwayTeamId(awayLineup.getLinuepInfo().getTeamId());
        } else {
            this.awayLineup = null;
            ErrorUtils.raise("Illegal 'Match' constructor call with NULL 'awayLineup'");
        }
        
        if (homeLineup != null) {
            this.homeLineup = homeLineup;
            this.matchInfo.setHomeTeamId(homeLineup.getLinuepInfo().getTeamId());
        } else {
            this.homeLineup = null;
            ErrorUtils.raise("Illegal 'Match' constructor call with NULL 'homeLineup'");
        }

    }

    public long getId() {
        return matchInfo.getMatchId();
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

    public boolean belongsToLeagueAndDate(long leagueId, LocalDate date) {
        return matchInfo.getLeagueId() == leagueId && matchInfo.getMatchDay().compareTo(date) == 0;
    }
    public boolean belongsToLeagueAndRound(long leagueId, int round) {
        return matchInfo.getLeagueId() == leagueId && matchInfo.getLeagueRound() == round;
    }

    public static Match getMatchDetail(long matchId) {
        var match = AssociationManager.getInstance().getMatchById(matchId);
        return Objects.requireNonNullElseGet(match, () -> getMatchFromDB(matchId));
    }
    private static Match getMatchFromDB(long matchId) {
        var matchInfo = (MatchInfo) GameDBManager.getInstance().getObjectById(MatchInfo.class, matchId);

        // TODO get away lineup
        // TODO get home lineup
        // TODO pbp not being saved anywhere
        // TODO AssociationManager should hold at least all teams and possibly also all matches in-memory

        var match = new Match(matchInfo, new Lineup(1, "a", "b", "c"), new Lineup(2, "d", "e", "f"));

        var dbList = GameDBManager.getInstance().getObjectsByQuery(MatchPlayByPlay.class, "matchId", matchId);
        if (Utils.listNotEmpty(dbList)) {
            var pbpList = new ArrayList<MatchPlayByPlay>();
            dbList.forEach(item -> pbpList.add((MatchPlayByPlay) item));
            match.setPlayByPlay(pbpList);
        }

        return match;
    }
    
}
