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
        return getPlayerInfo().getRegistered() == AssociationManager.getInstance().getClock().getYear();
    }

    public void register(int year) {
        getPlayerInfo().setRegistered(year);
        persist();
    }

}
