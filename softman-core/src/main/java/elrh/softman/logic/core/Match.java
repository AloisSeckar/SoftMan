package elrh.softman.logic.core;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.data.MatchInfo;
import elrh.softman.logic.core.data.MatchPlayByPlay;
import elrh.softman.logic.enums.MatchStatus;
import elrh.softman.logic.core.stats.*;
import elrh.softman.logic.interfaces.IMatchReporter;
import elrh.softman.utils.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            this.matchInfo.setAwayTeamId(awayLineup.getLineupInfo().getTeamId());
            this.matchInfo.setAwayLineupId(awayLineup.getLineupInfo().getLineupId());
        } else {
            this.awayLineup = null;
            ErrorUtils.raise("Illegal 'Match' constructor call with NULL 'awayLineup'");
        }
        
        if (homeLineup != null) {
            this.homeLineup = homeLineup;
            this.matchInfo.setHomeTeamId(homeLineup.getLineupInfo().getTeamId());
            this.matchInfo.setHomeLineupId(homeLineup.getLineupInfo().getLineupId());
        } else {
            this.homeLineup = null;
            ErrorUtils.raise("Illegal 'Match' constructor call with NULL 'homeLineup'");
        }

    }

    public UUID getId() {
        return matchInfo.getMatchId();
    }

    public void printPlayByPlay(IMatchReporter reporter) {
        playByPlay.forEach(pbp -> reporter.report(pbp.getPlay()));
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

    public boolean belongsToLeagueAndDate(UUID leagueId, LocalDate date) {
        return matchInfo.getLeagueId().equals(leagueId) && matchInfo.getMatchDay().compareTo(date) == 0;
    }
    public boolean belongsToLeagueAndRound(UUID leagueId, int round) {
        return matchInfo.getLeagueId().equals(leagueId) && matchInfo.getLeagueRound() == round;
    }

    public static Match getMatchDetail(UUID matchId) {
        return AssociationManager.getInstance().getMatchById(matchId);
    }

}
