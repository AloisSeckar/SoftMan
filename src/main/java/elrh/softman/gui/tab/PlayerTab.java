package elrh.softman.gui.tab;

import elrh.softman.gui.tile.PlayerInfoTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.db.orm.PlayerInfo;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.StringUtils;

public class PlayerTab extends HBox {

    private final PlayerInfoTile playerInfo = new PlayerInfoTile();

    private final TextArea statsOverview = new TextArea();

    private static PlayerTab INSTANCE;
    public static PlayerTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerTab();
        }
        return INSTANCE;
    }

    private PlayerTab() {
        super.getChildren().addAll(playerInfo, statsOverview);
    }

    public void reload(PlayerInfo info) {
        playerInfo.reload(info);

        statsOverview.clear();
        statsOverview.appendText("PLAYER                         | PA | AB |  H |  R | RBI |   AVG |  O |  IP | \n");
        var player = AssociationManager.getInstance().getPlayerById(info.getPlayerId());
        if (player != null) {
            player.getStats().forEach(record -> {
                var nameWithPos = record.getMatchId() + " " + record.getPlayerPos();
                statsOverview.appendText(StringUtils.rightPad(nameWithPos, 30, " ") + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBPA()), 2) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBAB()), 2) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBH()), 2) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBR()), 2) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBRB()), 3) + " | ");
                statsOverview.appendText(/*record.getAVG()*/ "x.xxx" + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getFPO()), 2) + " | ");
                statsOverview.appendText(/*record.getIP()*/ "x.xxx" + " | \n");
            });
        }
    }

}
