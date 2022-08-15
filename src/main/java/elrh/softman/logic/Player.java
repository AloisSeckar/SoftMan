package elrh.softman.logic;

import elrh.softman.db.GameDBManager;
import elrh.softman.db.orm.PlayerInfo;
import lombok.Data;

@Data
public class Player implements IDatabaseEntity {

    private PlayerInfo playerInfo;

    @Override
    public long getId() {
        return playerInfo.getPlayerId();
    }

    @Override
    public void persist() {
        GameDBManager.getInstance().savePlayer(this);
    }

    public boolean isActive() {
        // TODO  impl
        return true;
    }

}
