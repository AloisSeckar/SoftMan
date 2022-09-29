package elrh.softman.logic.core;

import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.db.orm.records.StatsRecord;
import elrh.softman.logic.interfaces.IDatabaseEntity;
import java.util.ArrayList;
import lombok.Data;

@Data
public class Player implements IDatabaseEntity {

    private PlayerInfo playerInfo = new PlayerInfo();

    private ArrayList<StatsRecord> stats = new ArrayList<>();

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

    public void register() {
        AssociationManager.getInstance().registerPlayer(this);
    }

}
