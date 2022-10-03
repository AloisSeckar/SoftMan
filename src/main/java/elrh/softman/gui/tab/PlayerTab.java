package elrh.softman.gui.tab;

import elrh.softman.gui.tile.PlayerInfoTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.db.orm.player.PlayerInfo;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import elrh.softman.utils.StatsUtils;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;

public class PlayerTab extends BorderPane implements IFocusedTeamListener {

    private final ComboBox<PlayerInfo> selectPlayerCB;
    private final PlayerInfoTile playerInfo = new PlayerInfoTile(false);

    private final TextArea statsOverview = new TextArea();

    private static PlayerTab INSTANCE;
    public static PlayerTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerTab();
        }
        return INSTANCE;
    }

    private PlayerTab() {
        var manager = AssociationManager.getInstance();
        var user = manager.getUser();

        var selectPlayerLabel = new Label(" Select player: ");
        selectPlayerCB = new ComboBox<>(FXCollections.observableList(user.getFocusedTeam().getPlayers()));
        selectPlayerCB.setMinWidth(220d);
        selectPlayerCB.setMaxWidth(220d);
        selectPlayerCB.valueProperty().addListener((ov, oldValue, newValue) -> reload(newValue));

        statsOverview.setStyle("-fx-font: 12px 'Courier New';");

        var attributesButton = new Button("Attributes");
        attributesButton.setAlignment(Pos.CENTER);
        var seasonStatsButton = new Button("Season stats");
        seasonStatsButton.setAlignment(Pos.CENTER);
        var carrierStatsButton = new Button("Carrier stats");
        carrierStatsButton.setAlignment(Pos.CENTER);

        var controlBox = new VBox(new HBox(selectPlayerLabel, selectPlayerCB), playerInfo, attributesButton, seasonStatsButton, carrierStatsButton);

        super.setLeft(controlBox);
        super.setCenter(statsOverview);
    }

    public void reload(PlayerInfo info) {
        selectPlayerCB.setValue(info);

        playerInfo.reload(info);

        statsOverview.clear();
        statsOverview.appendText("PLAYER                         |  PA |  AB |   R |   H |  2B |  3B |  HR |  SH |  SF |  BB |  HP |  SB |  CS |   K | RBI |   AVG |   SLG |  PO |   A |   E |    IP | \n");
        var player = AssociationManager.getInstance().getPlayerById(info.getPlayerId());
        if (player != null) {
            player.getStats().forEach(record -> {
                var nameWithPos = record.getMatchId() + " " + record.getPlayerStr();
                statsOverview.appendText(StringUtils.rightPad(nameWithPos, 30, " ") + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBPA()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBAB()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBR()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBH()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getB2B()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getB3B()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBHR()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBSH()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBSF()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBBB()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBHP()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBSB()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBCS()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBK()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getBRB()), 3) + " | ");
                statsOverview.appendText(StatsUtils.getAVG(record.getBAB(), record.getBH()) + " | ");
                statsOverview.appendText(StatsUtils.getSLG(record.getBAB(), record.getBH(), record.getB2B(), record.getB3B(), record.getBHR()) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getFPO()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getFA()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(String.valueOf(record.getFE()), 3) + " | ");
                statsOverview.appendText(StringUtils.leftPad(StatsUtils.getIP(record.getFIP()), 5) + " | \n");
            });
        }
    }

    @Override
    public void focusedTeamChanged(Team newlyFocusedTeam) {
        selectPlayerCB.setItems(FXCollections.observableList(newlyFocusedTeam.getPlayers()));
        selectPlayerCB.setValue(selectPlayerCB.getItems().get(0));
    }
}
