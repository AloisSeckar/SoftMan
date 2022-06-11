package elrh.softman.logic.stats;

import elrh.softman.constants.Constants;
import java.util.Arrays;
import javafx.scene.control.TextArea;
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

    public int getPointsInInning(int inning, boolean away) {
        if (inning > 0 && inning < this.innings) {
            if (away) {
                return awayPoints[inning];
            } else {
                return homePoints[inning];
            }
        }
        return 0;
    }
    
    public int getTotalPoints(boolean away) {
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

    public void printBoxScore(TextArea target) {
        StringBuilder lineSeparator = new StringBuilder();
        lineSeparator.append("---------");
        lineSeparator.append("-----".repeat(awayPoints.length));
        lineSeparator.append("---------------\n");

        StringBuilder sb = new StringBuilder(lineSeparator);
        
        sb.append("| TEAM  | ");
        for (int i = 1; i <= innings; i++) {
            sb.append(pad(i)).append(" | ");
        }
        sb.append(" R |  H |  E | \n");
        sb.append(lineSeparator);

        sb.append("| AWAY  | ");
        for (int i = 0; i < innings; i++) {
            sb.append(pad(awayPoints[i])).append(" | ");
        }
        sb.append(pad(sumPoints(awayPoints))).append(" | ");
        sb.append(pad(awayHits)).append(" | ");
        sb.append(pad(awayErrors)).append(" | \n");
        sb.append(lineSeparator);

        sb.append("| HOME  | ");
        for (int i = 0; i < innings; i++) {
            sb.append(pad(homePoints[i])).append(" | ");
        }
        sb.append(pad(sumPoints(homePoints))).append(" | ");
        sb.append(pad(homeHits)).append(" | ");
        sb.append(pad(homeErrors)).append(" | \n");
        sb.append(lineSeparator);

        target.appendText(sb + "\n");
    }

    private int sumPoints(int[] innings) {
        int ret = 0;

        for (int inning : innings) {
            ret += inning;
        }

        return ret;
    }

    private String pad(int number) {
        return StringUtils.leftPad(String.valueOf(number), 2, ' ');
    }

}
