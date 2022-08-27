package elrh.softman.utils.factory;

import elrh.softman.logic.db.SourcesDBManager;
import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.enums.PlayerGender;
import lombok.Getter;

public class PlayerFactory {

    public static PlayerInfo getRandomPlayerInfo(PlayerGender gender, int birth, int number) {
        PlayerInfo player =  new PlayerInfo(getRandomPlayerName(gender.toString().toLowerCase()), gender, birth, number);
        return player;
    }

    private static String getRandomPlayerName(String gender) {
        return SourcesDBManager.getInstance().getRandomFirstName(gender) + " " + SourcesDBManager.getInstance().getRandomLastName();
    }

}
