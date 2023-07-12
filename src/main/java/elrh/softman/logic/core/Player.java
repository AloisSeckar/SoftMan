package elrh.softman.logic.core;

import elrh.softman.logic.db.orm.player.PlayerAttributes;
import elrh.softman.logic.db.orm.player.PlayerInfo;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.db.orm.player.PlayerStats;
import java.util.ArrayList;

import elrh.softman.logic.enums.ActivityType;
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

    public void increaseFatigue(ActivityType activity) {
        PlayerAttributes attributes = playerInfo.getAttributes();
        int actualFatigue = activity.getBaseFatigue() - (attributes.getEndurance() / 10);
        attributes.setFatigue(Math.min(attributes.getFatigue() + actualFatigue, 100));
        attributes.persist();
    }

    public void decreaseFatigue() {
        PlayerAttributes attributes = playerInfo.getAttributes();
        int baseRecovery = 10;
        int actualRecovery = baseRecovery + (attributes.getRecovery() / 10);
        attributes.setFatigue(Math.max(attributes.getFatigue() - actualRecovery, 0));
        attributes.persist();
    }

}
