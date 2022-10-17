package elrh.softman.utils.factory;

import elrh.softman.logic.core.Club;
import javafx.scene.paint.Color;

public class ClubFactory {

    public static Club getClub(String name, String shortName) {
        Club newClub = new Club(name, shortName, "Unknown", "The Field");
        newClub.getClubInfo().setLogo("/img/teams/" + name.toLowerCase() + ".png");
        newClub.getClubInfo().persist();
        newClub.setColor(getColorForName(name));

        return newClub;
    }

    // TODO this is only relevant for testing teams...
    private static Color getColorForName(String name) {
        return switch (name) {
            case "REDS" -> Color.RED;
            case "BLUES" -> Color.BLUE;
            case "GREENS" -> Color.GREEN;
            case "YELLOWS" -> Color.YELLOW;
            case "BLACKS" -> Color.DARKGRAY;
            case "WHITES" -> Color.WHITE;
            case "SILVERS" -> Color.SILVER;
            case "VIOLETS" -> Color.VIOLET;
            case "BROWNS" -> Color.SADDLEBROWN;
            case "GOLDS" -> Color.GOLD;
            case "CYANS" -> Color.CYAN;
            case "PURPLES" -> Color.PURPLE;
            case "ORANGES" -> Color.ORANGE;
            case "LIMES" -> Color.LIMEGREEN;
            case "PINKS" -> Color.PINK;
            case "BLOODS" -> Color.DARKRED;
            default -> Color.GRAY;
        };
    }

}
