package elrh.softman.logic;

import elrh.softman.logic.core.lineup.Lineup;
import elrh.softman.logic.db.orm.MatchPlayByPlay;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.enums.MatchStatus;
import elrh.softman.logic.enums.PlayerPosition;
import static elrh.softman.logic.enums.StatsType.*;
import elrh.softman.logic.core.stats.BoxScore;
import elrh.softman.utils.Constants;
import elrh.softman.utils.StatsUtils;
import elrh.softman.utils.Utils;
import java.util.Random;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MatchSimulator {

    private static final Random random = new Random();

    private final TextArea target;
    private final Match match;

    private final BoxScore boxScore;
    private final Lineup awayLineup;
    private final Lineup homeLineup;
    
    private boolean inningStart = true;
    private int inning = 1;
    private boolean top = true;
    private int outs = 0;
    private int awayBatter = 0;
    private int homeBatter = 0;
    private Lineup battingLineup;
    private Lineup fieldingLineup;

    public MatchSimulator(Match match, TextArea target) {
        this.target = target;
        this.match = match;
        awayLineup = match.getAwayLineup();
        homeLineup = match.getHomeLineup();
        boxScore = match.getBoxScore();
    }

    // keep simulating until the end of the match
    public void simulateMatch() {
        while (!match.isFinished()) {
            simulateInning();
        }
    }

    // keep simulating until the end of current inning
    public void simulateInning() {
        int currentInning = inning;
        while (!match.isFinished() && inning == currentInning) {
            simulatePlay();
        }
    }

    // single new situation
    public void simulatePlay() {
        if (match.isScheduled()) {
            setUpMatch();
        }

        if (keepPlaying()) {
            if (inningStart) {
                if (top) {
                    appendText("\n\nINNING " + inning + "\n");
                    appendText("\n\nTOP\n");
                    battingLineup = awayLineup;
                    fieldingLineup = homeLineup;
                } else {
                    appendText("\n\nBOTTOM\n");
                    battingLineup = homeLineup;
                    fieldingLineup = awayLineup;
                }
                inningStart = false;
            }

            var pitcher = fieldingLineup.getCurrentPositionPlayer(PlayerPosition.PITCHER);
            var pitcherAttr = pitcher.getPlayer().getAttributes();

            appendText("PITCHER: " + pitcher + " (" + pitcherAttr.getPitchingSkill() + ")\n");

            var batter = battingLineup.getCurrentBatter(top ? awayBatter : homeBatter);
            if (batter != null) {
                var batterInfo = batter.getPlayer();
                var batterAttr = batterInfo.getAttributes();

                appendText("BATTER: " + batterInfo + " (" + batterAttr.getBattingSkill() + ")\n");

                int pitchQuality = pitcherAttr.getPitchingSkill() + random.nextInt(100);
                int hitQuality = batterAttr.getBattingSkill() + random.nextInt(100);

                appendText(pitchQuality + " vs. " + hitQuality + "\n");

                if (hitQuality >= pitchQuality) {
                    if (hitQuality - pitchQuality > 25) {
                        appendText(batter + " SCORED\n");
                        StatsUtils.incH(batter);
                        batter.getStats().inc(BR);
                        batter.getStats().inc(BRB);
                        boxScore.addHit(top);
                        boxScore.addPoint(inning, top);
                    } else {

                        int randomLocation = random.nextInt(9);
                        var fielder = fieldingLineup.getCurrentBatter(randomLocation);

                        if (fielder != null) {
                            int fieldingQuality = fielder.getPlayer().getAttributes().getFieldingSkill() + random.nextInt(100);
                            if (hitQuality >= fieldingQuality) {
                                if (random.nextBoolean()) {
                                    appendText(batter + " reached after a hit\n");
                                    StatsUtils.incH(batter);
                                    boxScore.addHit(top);
                                } else {
                                    StatsUtils.incAB(batter);
                                    appendText(batter + " reached otherwise\n");
                                }
                            } else {
                                appendText(batter + " is OUT\n");
                                StatsUtils.incAB(batter);
                                fielder.getStats().inc(FPO);
                                StatsUtils.incIP(fieldingLineup);
                                outs++;
                            }
                        } else {
                            appendText(batter + " reached after a hit\n");
                            StatsUtils.incH(batter);
                            boxScore.addHit(top);
                        }
                    }
                } else {
                    appendText(batter + " is OUT\n");
                    StatsUtils.incAB(batter);
                    StatsUtils.incIP(fieldingLineup);
                    outs++;
                }
            } else {
                appendText("Position not filled => OUT\n");
                outs++;
            }

            if (top) {
                awayBatter++;
                if (awayBatter > 8) {
                    awayBatter = 0;
                }
            } else {
                homeBatter++;
                if (homeBatter > 8) {
                    homeBatter = 0;
                }
            }

            if (outs == 3) {
                getScore();
                swapLineups();
            }

            if (!keepPlaying()) {
                wrapUpMatch();
            }
        }
    }


    ///////
    private void setUpMatch() {
        match.getMatchInfo().setStatus(MatchStatus.ACTIVE);
        appendText("\n\nGAME BETWEEN " + awayLineup.getTeamName() + " AND " + homeLineup.getTeamName() + "\n");
    }

    private void wrapUpMatch() {
        boxScore.trimIfNeeded(inning);
        appendText("\n\nGAME OVER\n\n");
        match.getMatchInfo().setHomeTeamFinishedBatting(top);
        match.getMatchInfo().setStatus(MatchStatus.FINISHED);

        appendText("\n\nSTATS\n");
        printStats(awayLineup);
        printStats(homeLineup);

        // TODO save the match into correct league
        AssociationManager.getInstance().getLeagues(Constants.START_YEAR).get(0).saveMatch(match);
    }

    private boolean keepPlaying() {
        boolean ret = true;

        int awayPoints = boxScore.getTotalPoints(true);
        int homePoints = boxScore.getTotalPoints(false);

        if (top) {
            // match can end only before next inning
            // after that away Lineup will always finnish their batting
            if (inningStart) {
                if (inning >= 4 && (Math.abs(awayPoints - homePoints) >= 15)) {
                    ret = false;
                } else if (inning >= 5 && (Math.abs(awayPoints - homePoints) >= 10)) {
                    ret = false;
                } else if (inning >= 6 && (Math.abs(awayPoints - homePoints) >= 7)) {
                    ret = false;
                } else if (inning >= 8 && (Math.abs(homePoints - awayPoints) > 0)) {
                    ret = false;
                }

                if (!ret) {
                    // revert values for correct box score (new inning didn't actually start)
                    inning--;
                    top = false;
                }
            }
        } else {
            // match can end anytime homeLineup gets in the lead or triggers mercy rule
            if (inning >= 3 && homePoints - awayPoints >= 15) {
                ret = false;
            } else if (inning >= 4 && homePoints - awayPoints >= 10) {
                ret = false;
            } else if (inning >= 5 && homePoints - awayPoints >= 7) {
                ret = false;
            } else if (inning >= 7 && homePoints - awayPoints > 0) {
                ret = false;
            }
        }

        return ret;
    }
    private void swapLineups() {
        outs = 0;
        top = !top;
        if (top) {
            inning++;
        }
        inningStart = true;
    }

    private void getScore() {
        appendText("\n\n" + awayLineup.getTeamName() + ": " + boxScore.getTotalPoints(true) + "\n");
        appendText(homeLineup.getTeamName() + ": " + boxScore.getTotalPoints(false) + "\n");
    }

    private void printStats(Lineup lineup) {
        appendText("\n\n" + lineup.getTeamName() + "\n");
        appendText("PLAYER                         | PA | AB |  H |  R | RBI |   AVG |  O |  IP | \n");

        for (int i = 0; i < Lineup.POSITION_PLAYERS; i++) {
            var players = lineup.getPositionPlayers()[i];
            if (Utils.listNotEmpty(players)) {
                players.forEach(playerRecord -> {
                    var nameWithPos = playerRecord.getPlayer().getName() + ", " + playerRecord.getPosition().toString();
                    appendText(StringUtils.rightPad(nameWithPos, 30, " ") + " | ");
                    var stats = playerRecord.getStats();
                    var record = playerRecord.getStats().getStatsRecord();
                    appendText(StringUtils.leftPad(String.valueOf(record.getBPA()), 2) + " | ");
                    appendText(StringUtils.leftPad(String.valueOf(record.getBAB()), 2) + " | ");
                    appendText(StringUtils.leftPad(String.valueOf(record.getBH()), 2) + " | ");
                    appendText(StringUtils.leftPad(String.valueOf(record.getBR()), 2) + " | ");
                    appendText(StringUtils.leftPad(String.valueOf(record.getBRB()), 3) + " | ");
                    appendText(stats.getAVG() + " | ");
                    appendText(StringUtils.leftPad(String.valueOf(record.getFPO()), 2) + " | ");
                    appendText(stats.getIP() + " | \n");
                });
            }
        }

    }
    
    private void appendText(String text) {
        target.appendText(text);
        match.getPlayByPlay().add(new MatchPlayByPlay(match.getMatchInfo().getMatchId(), match.getPlayByPlay().size() + 1, text));
    }

}
