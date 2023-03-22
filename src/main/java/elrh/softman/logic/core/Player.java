package elrh.softman.logic.core;

import elrh.softman.logic.db.orm.player.PlayerInfo;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.db.orm.player.PlayerStats;
import java.util.ArrayList;
import lombok.Data;

@Data
public class Player {

    private PlayerInfo playerInfo = new PlayerInfo();

    private final ArrayList<PlayerStats> stats = new ArrayList<>();
    private final PlayerStats seasonTotal = new PlayerStats(); // TODO deal with multiple seasons?
    {
        seasonTotal.setMatchStr("Season total");
    }

    public long getId() {
        return playerInfo.getPlayerId();
    }

    public boolean isActive() {
        return getPlayerInfo().getRegistered() == AssociationManager.getInstance().getClock().getYear();
    }

    public void register() {
        AssociationManager.getInstance().registerPlayer(this);
    }

}
