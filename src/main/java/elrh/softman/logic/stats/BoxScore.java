package elrh.softman.logic.stats;

import elrh.softman.constants.Constants;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

public class BoxScore {

    private int innings = Constants.INNINGS;

    private int[] awayPoints = new int[innings];
    private int awayHits;
    private int awayErrors;

    private int[] homePoints = new int[innings];
    private int homeHits;
    private int homeErrors;

    public void addPoint(int inning, boolean away) {
        if (inning > 0) {
            if (inning > innings) {
                innings = inning;
                awayPoints = Arrays.copyOf(awayPoints, innings);
                homePoints = Arrays.copyOf(homePoints, innings);
            }

            if (away) {
                awayPoints[inning - 1]++;
            } else {
                homePoints[inning - 1]++;
            }
        }
    }

    public void addHit(boolean away) {
        if (away) {
            awayHits++;
        } else {
            homeHits++;
        }
    }

    public void addError(boolean away) {
        if (away) {
            awayErrors++;
        } else {
            homeErrors++;
        }
    }

    public int getInnings() {
        return innings;
    }
    
    public int getPoints(boolean away) {
        if (away) {
            return sumPoints(awayPoints);
        } else {
            return sumPoints(homePoints);
        }
    }
    
    public int getHits(boolean away) {
        if (away) {
            return awayHits;
        } else {
            return homeHits;
        }
    }
    
    public int getErrors(boolean away) {
        if (away) {
            return awayErrors;
        } else {
            return homeErrors;
        }
    }

    public void printBoxScore() {
        
        printLineSeparator();
        
        System.out.print("| TEAM  | ");
        for (int i = 1; i <= innings; i++) {
            System.out.print(pad(i) + " | ");
        }
        System.out.println(" R |  H |  E | ");
        
        printLineSeparator();

        System.out.print("| AWAY  | ");
        for (int i = 0; i < innings; i++) {
            System.out.print(pad(awayPoints[i]) + " | ");
        }
        System.out.print(pad(sumPoints(awayPoints)) + " | ");
        System.out.print(pad(awayHits) + " | ");
        System.out.println(pad(awayErrors) + " | ");

        printLineSeparator();

        System.out.print("| HOME  | ");
        for (int i = 0; i < innings; i++) {
            System.out.print(pad(homePoints[i]) + " | ");
        }
        System.out.print(pad(sumPoints(homePoints)) + " | ");
        System.out.print(pad(homeHits) + " | ");
        System.out.println(pad(homeErrors) + " | ");

        printLineSeparator();
    }

    private void printLineSeparator() {
        System.out.print("---------");
        for (int i = 0; i < awayPoints.length; i++) {
            System.out.print("-----");
        }
        System.out.println("---------------");
    }

    private int sumPoints(int[] innings) {
        int ret = 0;

        for (int i = 0; i < innings.length; i++) {
            ret += innings[i];
        }

        return ret;
    }

    private String pad(int number) {
        return StringUtils.leftPad(String.valueOf(number), 2, ' ');
    }

}
