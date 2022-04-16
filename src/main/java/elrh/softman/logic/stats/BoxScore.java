package elrh.softman.logic.stats;

import elrh.softman.constants.Constants;
import elrh.softman.gui.view.tab.StandingsTab;
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
        StringBuilder sb = new StringBuilder();
        
        printLineSeparator(sb);
        
        sb.append("| TEAM  | ");
        for (int i = 1; i <= innings; i++) {
            sb.append(pad(i)).append(" | ");
        }
        sb.append(" R |  H |  E | ");
        StandingsTab.getInstance().writeIntoConsole(sb.toString());
        
        printLineSeparator(sb);

        sb.append("| AWAY  | ");
        for (int i = 0; i < innings; i++) {
            sb.append(pad(awayPoints[i])).append(" | ");
        }
        sb.append(pad(sumPoints(awayPoints))).append(" | ");
        sb.append(pad(awayHits)).append(" | ");
        sb.append(pad(awayErrors)).append(" | ");
        StandingsTab.getInstance().writeIntoConsole(sb.toString());

        printLineSeparator(sb);

        sb.append("| HOME  | ");
        for (int i = 0; i < innings; i++) {
            sb.append(pad(homePoints[i])).append(" | ");
        }
        sb.append(pad(sumPoints(homePoints))).append(" | ");
        sb.append(pad(homeHits)).append(" | ");
        sb.append(pad(homeErrors)).append(" | ");
        StandingsTab.getInstance().writeIntoConsole(sb.toString());

        printLineSeparator(sb);
    }

    private void printLineSeparator(StringBuilder sb) {
        sb.setLength(0);
        sb.append("---------");
        sb.append("-----".repeat(awayPoints.length));
        sb.append("---------------");
        StandingsTab.getInstance().writeIntoConsole(sb.toString());
        sb.setLength(0);
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
