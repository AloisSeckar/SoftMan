package elrh.softman.logic;

import elrh.softman.db.orm.PlayerAttributes;
import elrh.softman.db.orm.PlayerInfo;
import elrh.softman.logic.stats.BoxScore;
import java.util.Random;
import javafx.scene.control.TextArea;

public class MatchSimulator {

    private static final Random random = new Random();

    private final TextArea target;
    private final Match match;

    private final BoxScore boxScore;
    private final Team awayTeam;
    private final Team homeTeam;
    
    private boolean header = true;
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

    public void playMatch() {
        if (header) {
            if (top && playNextInning()) {
                if (inning == 1) {
                    match.getMatchInfo().setStatus(MatchStatus.IN_PROGRESS);
                    target.appendText("\n\nGAME BETWEEN " + awayTeam.getName() + " AND " + homeTeam.getName() + "\n");
                }
                target.appendText("\n\nINNING " + inning + "\n");
                target.appendText("\n\nTOP\n");
            } else if (continueInning()) {
                target.appendText("\n\nBOTTOM\n");
            }
            header = false;
        }

        if (playNextInning()) {
            if ((top || continueInning()) && outs < 3) {
                simulatePlay();
            }
            if (outs == 3) {
                getScore();
                swapTeams();
            }
        }

        if (!continueInning()) {
            boxScore.printBoxScore(target);
            target.appendText("\n\nGAME OVER\n\n");
            match.getMatchInfo().setStatus(MatchStatus.PLAYED);
        }
    }


    public void simulateMatch() {
        match.getMatchInfo().setStatus(MatchStatus.IN_PROGRESS);

        while (playNextInning()) {
            target.appendText("\n\nINNING " + inning + "\n");

            target.appendText("\n\nTOP\n");
            simulateInning();

            if (continueInning()) {
                target.appendText("\n\nBOTTOM\n");
                simulateInning();
            }
        }

        target.appendText("\n\nGAME OVER\n\n");
        match.getMatchInfo().setStatus(MatchStatus.PLAYED);
    }
    public void simulateInning() {
        while (outs < 3) {
            simulatePlay();
        }
        getScore();
        swapTeams();
    }

    private void simulatePlay() {
        PlayerInfo pitcher = top ? homeTeam.getFielder(Position.PITCHER) : awayTeam.getFielder(Position.PITCHER);
        PlayerAttributes pitcherAttr = pitcher.getAttributes();

        target.appendText("PITCHER: " + pitcher + " (" + pitcherAttr.getPitchingSkill() + ")\n");

        LineupPosition batter = top ? awayTeam.getBatter(awayBatter) : homeTeam.getBatter(homeBatter);
        if (batter != null) {
            PlayerInfo batterInfo = batter.getPlayer();
            PlayerAttributes batterAttr = batterInfo.getAttributes();

            target.appendText("BATTER: " + batterInfo + " (" + batterAttr.getBattingSkill() + ")\n");

            int pitchQuality = pitcherAttr.getPitchingSkill() + random.nextInt(100);
            int hitQuality = batterAttr.getBattingSkill() + random.nextInt(100);

            target.appendText(pitchQuality + " vs. " + hitQuality + "\n");

            if (hitQuality >= pitchQuality) {
                if (hitQuality - pitchQuality > 25) {
                    target.appendText(batter + " SCORED\n");
                    boxScore.addHit(top);
                    boxScore.addPoint(inning, top);
                } else {

                    int randomLocation = random.nextInt(9);
                    PlayerInfo fielder = top ? homeTeam.getFielder(randomLocation) : awayTeam.getFielder(randomLocation);

                    if (fielder != null) {
                        int fieldingQuality = fielder.getAttributes().getFieldingSkill()+ random.nextInt(100);
                        if (hitQuality >= fieldingQuality) {
                            if (random.nextBoolean()) {
                                target.appendText(batter + " reached after a hit\n");
                                boxScore.addHit(top);
                            } else {
                                target.appendText(batter + " reached otherwise\n");
                            }
                        } else {
                            target.appendText(batter + " is OUT\n");
                            outs++;
                        }
                    } else {
                        target.appendText(batter + " reached after a hit\n");
                        boxScore.addHit(top);
                    }
                }
            } else {
                target.appendText(batter + " is OUT\n");
                outs++;
            }
        } else {
            target.appendText("Position not filled => OUT\n");
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
    }

    private void swapTeams() {
        header = true;
        outs = 0;
        top = !top;
        if (top) {
            inning++;
        }
    }

    private void getScore() {
        target.appendText("\n\n" + awayTeam.getName() + ": " + boxScore.getPoints(true) + "\n");
        target.appendText(homeTeam.getName() + ": " + boxScore.getPoints(false) + "\n");
    }

    private boolean playNextInning() {
        boolean ret = true;
        
        int awayPoints = boxScore.getPoints(true);
        int homePoints = boxScore.getPoints(false);
        
        if (inning >= 4 && (awayPoints - homePoints >= 15 || homePoints - awayPoints >= 15)) {
            ret = false;
        } else if (inning >= 5 && (awayPoints - homePoints >= 10 || homePoints - awayPoints >= 10)) {
            ret = false;
        } else if (inning >= 6 && (awayPoints - homePoints >= 7 || homePoints - awayPoints >= 7)) {
            ret = false;
        } else if (inning >= 8 && (homePoints - awayPoints > 0 || awayPoints - homePoints > 0)) {
            ret = false;
        }
        
        return ret;
    }

    private boolean continueInning() {
        boolean ret = true;
        
        int awayPoints = boxScore.getPoints(true);
        int homePoints = boxScore.getPoints(false);
        
        if (!top) {
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

}
