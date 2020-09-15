package elrh.softman.logic;

import java.util.Random;

public class MatchSimulator {

    private static final Random random = new Random();

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

        int inning = 1;
        while (inning < 8 || homePoints == awayPoints) {
            System.out.println("INNING " + inning);

            System.out.println("TOP");
            simulateInning(true);
            getScore();

            System.out.println("BOTTOM");
            simulateInning(false);
            getScore();

            inning++;
        }

        System.out.println("GAME OVER");
    }

    ////////////////////////////////////////////////////////////////////////////
    private static void simulateInning(boolean top) {
        Player pitcher = top ? homeTeam.getFielder(Position.PITCHER) : awayTeam.getFielder(Position.PITCHER);
        
        System.out.println("PITCHER: " + pitcher.toString() + " (" + pitcher.getPitchingSkill() + ")");
        
        int outs = 0;
        while (outs < 3) {
            LineupPosition batter = top ? awayTeam.getBatter(awayBatter) : homeTeam.getBatter(homeBatter);
            if (batter != null) {
                
                System.out.println("BATTER: " + batter.getPlayer().toString() + " (" + batter.getPlayer().getBattingSkill() + ")");
                
                int pitchQuality = pitcher.getPitchingSkill() + random.nextInt(100);
                int hitQuality = batter.getPlayer().getBattingSkill() + random.nextInt(100);
                
                System.out.println(pitchQuality + " vs. " + hitQuality);
                
                if (hitQuality >= pitchQuality) {
                    if (hitQuality - pitchQuality > 50) {
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
        System.out.println(awayTeam.getName() + ": " + awayPoints);
        System.out.println(homeTeam.getName() + ": " + homePoints);
    }

}
