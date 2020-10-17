package elrh.softman.logic;

import elrh.softman.db.orm.PlayerInfo;
import elrh.softman.gui.view.MainView;
import elrh.softman.logic.stats.BoxScore;
import java.util.Random;

public class MatchSimulator {

    private static final Random random = new Random();
    
    private static int inning;
    private static boolean top;
    
    private static BoxScore boxScore;
    
    private static Team awayTeam;
    private static int awayBatter;

    private static Team homeTeam;
    private static int homeBatter;

    public static void simulateMatch(Match match) {

        awayTeam = match.getAwayTeam();
        homeTeam = match.getHomeTeam();
        boxScore = match.getBoxScore();
        
        homeBatter = 0;
        awayBatter = 0;
        
        writeIntoConsole("GAME BETWEEN " + awayTeam.getName() + " AND " + homeTeam.getName() );

        top = true;
        inning = 1;
        while (playNextInning()) {
            writeIntoConsole("\n\nINNING " + inning);

            writeIntoConsole("\n\nTOP");
            simulateInning();
            getScore();

            top = false;
            if (continueInning()) {
                writeIntoConsole("\n\nBOTTOM");
                simulateInning();
                getScore();
            }

            writeIntoConsole("\n\nINNING " + inning);
            inning++;
            top = true;
        }

        writeIntoConsole("\n\nGAME OVER");
    }

    ////////////////////////////////////////////////////////////////////////////
    private static void simulateInning() {
        PlayerInfo pitcher = top ? homeTeam.getFielder(Position.PITCHER) : awayTeam.getFielder(Position.PITCHER);
        
        writeIntoConsole("PITCHER: " + pitcher.toString() + " (" + pitcher.getStats().getPitchingSkill() + ")");
        
        int outs = 0;
        while ((top || continueInning()) && outs < 3) {
            LineupPosition batter = top ? awayTeam.getBatter(awayBatter) : homeTeam.getBatter(homeBatter);
            if (batter != null) {
                
                writeIntoConsole("BATTER: " + batter.getPlayer().toString() + " (" + batter.getPlayer().getStats().getBattingSkill() + ")");
                
                int pitchQuality = pitcher.getStats().getPitchingSkill() + random.nextInt(100);
                int hitQuality = batter.getPlayer().getStats().getBattingSkill() + random.nextInt(100);
                
                writeIntoConsole(pitchQuality + " vs. " + hitQuality);
                
                if (hitQuality >= pitchQuality) {
                    if (hitQuality - pitchQuality > 25) {
                        writeIntoConsole(batter + " SCORED");
                        boxScore.addHit(top);
                        boxScore.addPoint(inning, top);
                    } else {
                        
                        int randomLocation = random.nextInt(9);
                        PlayerInfo fielder = top ? homeTeam.getFielder(randomLocation) : awayTeam.getFielder(randomLocation);
                        
                        if (fielder != null) {
                            int fieldingQuality = fielder.getStats().getFieldingSkill()+ random.nextInt(100);
                            if (hitQuality >= fieldingQuality) {
                                if (random.nextBoolean()) {
                                    writeIntoConsole(batter + " reached after a hit");
                                    boxScore.addHit(top);
                                } else {
                                    writeIntoConsole(batter + " reached otherwise");
                                }
                            } else {
                                writeIntoConsole(batter + " is OUT");
                                outs++;
                            }
                        } else {
                            writeIntoConsole(batter + " reached after a hit");
                            boxScore.addHit(top);
                        }
                    }
                } else {
                    writeIntoConsole(batter + " is OUT");
                    outs++;
                }
            } else {
                writeIntoConsole("Position not filled => OUT");
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
    }

    private static void getScore() {
        writeIntoConsole("\n\n" + awayTeam.getName() + ": " + boxScore.getPoints(true));
        writeIntoConsole(homeTeam.getName() + ": " + boxScore.getPoints(false));
    }

    private static boolean playNextInning() {
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

    private static boolean continueInning() {
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
    
    private static void writeIntoConsole(String message) {
        MainView.getInstance().writeIntoConsole(message);
    }

}
