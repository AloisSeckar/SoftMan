package elrh.softman.utils.factory;

import elrh.softman.logic.core.Club;
import javafx.scene.paint.Color;

public class ClubFactory {

    public static Club getClub(String name, String shortName, String logo, Color color) {
        Club newClub = new Club(name, shortName, "Unknown", "The Field");
        newClub.getClubInfo().setLogo("/img/teams/" + logo + ".jpg");
        newClub.getClubInfo().persist();
        newClub.setColor(color);
        return newClub;
    }

}
