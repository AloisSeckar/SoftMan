package elrh.softman.utils.factory;

import elrh.softman.logic.core.Club;
import javafx.scene.paint.Color;

public class ClubFactory {

    public static Club getClub(String name) {
        Club newClub = new Club(name, "Unknown", "The Field");
        newClub.getClubInfo().setLogo("/img/teams/" + name.toLowerCase() + ".png");
        newClub.setColor(getColorForName(name));
        newClub.persist();


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
            default -> Color.GRAY;
        };
    }

}
