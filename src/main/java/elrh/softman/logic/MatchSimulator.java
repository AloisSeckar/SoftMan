package elrh.softman.logic;

import elrh.softman.db.orm.MatchPlayByPlay;
import elrh.softman.db.orm.PlayerAttributes;
import elrh.softman.db.orm.PlayerInfo;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.enums.MatchStatus;
import elrh.softman.logic.enums.Position;
import elrh.softman.logic.core.lineup.LineupPosition;
import elrh.softman.logic.core.stats.BoxScore;
import java.util.Random;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchSimulator {

    private static final Random random = new Random();

    private final TextArea target;
    private final Match match;

    private final BoxScore boxScore;
    private final Team awayTeam;
    private final Team homeTeam;
    
    private boolean inningStart = true;
    private int inning = 1;
    private boolean top = true;
    private int outs = 0;
    private int awayBatter = 0;
    private int homeBatter = 0;

    public MatchSimulator(Match match, TextArea target) {
        this.target = target;
        this.match = match;
        awayTeam = match.getAwayTeam();
        homeTeam = match.getHomeTeam();
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
                } else {
                    appendText("\n\nBOTTOM\n");
                }
                inningStart = false;
            }

            PlayerInfo pitcher = top ? homeTeam.getFielder(Position.PITCHER) : awayTeam.getFielder(Position.PITCHER);
            PlayerAttributes pitcherAttr = pitcher.getAttributes();

            appendText("PITCHER: " + pitcher + " (" + pitcherAttr.getPitchingSkill() + ")\n");

            LineupPosition batter = top ? awayTeam.getBatter(awayBatter) : homeTeam.getBatter(homeBatter);
            if (batter != null) {
                PlayerInfo batterInfo = batter.getPlayer();
                PlayerAttributes batterAttr = batterInfo.getAttributes();

                appendText("BATTER: " + batterInfo + " (" + batterAttr.getBattingSkill() + ")\n");

                int pitchQuality = pitcherAttr.getPitchingSkill() + random.nextInt(100);
                int hitQuality = batterAttr.getBattingSkill() + random.nextInt(100);

                appendText(pitchQuality + " vs. " + hitQuality + "\n");

                if (hitQuality >= pitchQuality) {
                    if (hitQuality - pitchQuality > 25) {
                        appendText(batter + " SCORED\n");
                        boxScore.addHit(top);
                        boxScore.addPoint(inning, top);
                    } else {

                        int randomLocation = random.nextInt(9);
                        PlayerInfo fielder = top ? homeTeam.getFielder(randomLocation) : awayTeam.getFielder(randomLocation);

                        if (fielder != null) {
                            int fieldingQuality = fielder.getAttributes().getFieldingSkill() + random.nextInt(100);
                            if (hitQuality >= fieldingQuality) {
                                if (random.nextBoolean()) {
                                    appendText(batter + " reached after a hit\n");
                                    boxScore.addHit(top);
                                } else {
                                    appendText(batter + " reached otherwise\n");
                                }
                            } else {
                                appendText(batter + " is OUT\n");
                                outs++;
                            }
                        } else {
                            appendText(batter + " reached after a hit\n");
                            boxScore.addHit(top);
                        }
                    }
                } else {
                    appendText(batter + " is OUT\n");
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
                swapTeams();
            }

            if (!keepPlaying()) {
                wrapUpMatch();
            }
        }
    }


    ///////
    private void setUpMatch() {
        match.getMatchInfo().setStatus(MatchStatus.ACTIVE);
        appendText("\n\nGAME BETWEEN " + awayTeam.getName() + " AND " + homeTeam.getName() + "\n");
    }

    private void wrapUpMatch() {
        boxScore.trimIfNeeded(inning);
        appendText("\n\nGAME OVER\n\n");
        match.getMatchInfo().setHomeTeamFinishedBatting(top);
        match.getMatchInfo().setStatus(MatchStatus.FINISHED);
        AssociationManager.getInstance().getPlayerLeague().saveMatch(match);
    }

    private boolean keepPlaying() {
        boolean ret = true;

        int awayPoints = boxScore.getTotalPoints(true);
        int homePoints = boxScore.getTotalPoints(false);

        if (top) {
            // match can end only before next inning
            // after that away team will always finnish their batting
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
            // match can end anytime hometeam gets in the lead or triggers mercy rule
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
    private void swapTeams() {
        outs = 0;
        top = !top;
        if (top) {
            inning++;
        }
        inningStart = true;
    }

    private void getScore() {
        appendText("\n\n" + awayTeam.getName() + ": " + boxScore.getTotalPoints(true) + "\n");
        appendText(homeTeam.getName() + ": " + boxScore.getTotalPoints(false) + "\n");
    }
    
    private void appendText(String text) {
        target.appendText(text);
        match.getPlayByPlay().add(new MatchPlayByPlay(match.getMatchInfo().getMatchId(), match.getPlayByPlay().size() + 1, text));
    }

}
