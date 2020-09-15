package elrh.softman.logic;

import java.util.Random;

public class MatchSimulator {

    private static final Random random = new Random();
    
    private static int inning;
    private static boolean top;

    private static Team awayTeam;
    private static int awayPoints;
    private static int awayBatter;

    private static Team homeTeam;
    private static int homePoints;
    private static int homeBatter;

    public static void simulateMatch(Team awayTeam, Team homeTeam) {

        MatchSimulator.awayTeam = awayTeam;
        MatchSimulator.homeTeam = homeTeam;

        homePoints = 0;
        awayPoints = 0;
        homeBatter = 0;
        awayBatter = 0;
        
        System.out.println("GAME BETWEEN " + awayTeam.getName() + " AND " + homeTeam.getName() );

        top = true;
        inning = 1;
        while (playNextInning()) {
            System.out.println("\n\nINNING " + inning);

            System.out.println("\n\nTOP");
            simulateInning();
            getScore();

            top = false;
            if (continueInning()) {
                System.out.println("\n\nBOTTOM");
                simulateInning();
                getScore();
            }

            System.out.println("\n\nINNING " + inning);
            inning++;
            top = true;
        }

        System.out.println("\n\nGAME OVER");
    }

    ////////////////////////////////////////////////////////////////////////////
    private static void simulateInning() {
        Player pitcher = top ? homeTeam.getFielder(Position.PITCHER) : awayTeam.getFielder(Position.PITCHER);
        
        System.out.println("PITCHER: " + pitcher.toString() + " (" + pitcher.getPitchingSkill() + ")");
        
        int outs = 0;
        while ((top || continueInning()) && outs < 3) {
            LineupPosition batter = top ? awayTeam.getBatter(awayBatter) : homeTeam.getBatter(homeBatter);
            if (batter != null) {
                
                System.out.println("BATTER: " + batter.getPlayer().toString() + " (" + batter.getPlayer().getBattingSkill() + ")");
                
                int pitchQuality = pitcher.getPitchingSkill() + random.nextInt(100);
                int hitQuality = batter.getPlayer().getBattingSkill() + random.nextInt(100);
                
                System.out.println(pitchQuality + " vs. " + hitQuality);
                
                if (hitQuality >= pitchQuality) {
                    if (hitQuality - pitchQuality > 25) {
                        System.out.println(batter + " SCORED");
                        if (top) {
                            awayPoints++;
                        } else {
                            homePoints++;
                        }
                    } else {
                        
                        int randomLocation = random.nextInt(9);
                        Player fielder = top ? homeTeam.getFielder(randomLocation) : awayTeam.getFielder(randomLocation);
                        
                        if (fielder != null) {
                            int fieldingQuality = fielder.getFieldingSkill()+ random.nextInt(100);
                            if (hitQuality >= fieldingQuality) {
                                System.out.println(batter + " reached");
                            } else {
                                System.out.println(batter + " is OUT");
                                outs++;
                            }
                        } else {
                            System.out.println(batter + " reached");
                        }
                    }
                } else {
                    System.out.println(batter + " is OUT");
                    outs++;
                }
            } else {
                System.out.println("Position not filled => OUT");
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
        System.out.println("\n\n" + awayTeam.getName() + ": " + awayPoints);
        System.out.println(homeTeam.getName() + ": " + homePoints);
    }

    private static boolean playNextInning() {
        boolean ret = true;
        
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
