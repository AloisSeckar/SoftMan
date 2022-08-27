package elrh.softman.utils.factory;

import elrh.softman.logic.db.SourcesDBManager;
import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.enums.PlayerGender;

public class PlayerFactory {

    public static PlayerInfo getRandomPlayerInfo(PlayerGender gender, int birth, int number) {
        return new PlayerInfo(getRandomPlayerName(), gender, birth, number);
    }

    private static String getRandomPlayerName() {
        return SourcesDBManager.getInstance().getRandomFirstName() + " " + SourcesDBManager.getInstance().getRandomLastName();
    }

}
