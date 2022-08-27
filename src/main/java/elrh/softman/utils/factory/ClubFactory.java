package elrh.softman.utils.factory;

import elrh.softman.logic.core.Club;

public class ClubFactory {

    public static Club getClub(String name) {
        Club newClub = new Club(name, "Unknown", "The Field");
        newClub.getClubInfo().setLogo("/img/teams/" + name.toLowerCase() + ".png");
        newClub.persist();

        return newClub;
    }

}
