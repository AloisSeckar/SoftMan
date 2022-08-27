package elrh.softman.utils.factory;

import elrh.softman.logic.db.SourcesDBManager;
import elrh.softman.logic.db.orm.PlayerInfo;

public class PlayerFactory {

    public static PlayerInfo getRandomPlayerInfo(int number) {
        return new PlayerInfo(getRandomPlayerName(), number);
    }

    private static String getRandomPlayerName() {
        return SourcesDBManager.getInstance().getRandomFirstName() + " " + SourcesDBManager.getInstance().getRandomLastName();
    }

}
