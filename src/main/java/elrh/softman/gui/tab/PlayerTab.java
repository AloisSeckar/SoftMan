package elrh.softman.gui.tab;

import elrh.softman.gui.tile.PlayerAttributesTile;
import elrh.softman.gui.tile.PlayerInfoTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.db.orm.player.PlayerInfo;
import elrh.softman.logic.db.orm.player.PlayerStats;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import elrh.softman.utils.StatsUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;

public class PlayerTab extends BorderPane implements IFocusedTeamListener {

    private final ComboBox<PlayerInfo> selectPlayerCB;
    private final PlayerInfoTile playerInfo = new PlayerInfoTile(false);
    private final PlayerAttributesTile playerAttributesTA = new PlayerAttributesTile();
    private final TextArea seasonStatsTA = new TextArea();
    private final TextArea careerStatsTA = new TextArea();

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

        seasonStatsTA.getStyleClass().add("font-courier-12");
        careerStatsTA.getStyleClass().add("font-courier-12");
        careerStatsTA.setText("Career stats");

        var attributesButton = new Button("Attributes");
        attributesButton.setAlignment(Pos.CENTER);
        attributesButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> Platform.runLater(() -> super.setCenter(playerAttributesTA)));

        var seasonStatsButton = new Button("Season stats");
        seasonStatsButton.setAlignment(Pos.CENTER);
        seasonStatsButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> Platform.runLater(() -> super.setCenter(seasonStatsTA)));

        var carrierStatsButton = new Button("Carrier stats");
        carrierStatsButton.setAlignment(Pos.CENTER);
        carrierStatsButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> Platform.runLater(() -> super.setCenter(careerStatsTA)));

        var controlBox = new VBox(new HBox(selectPlayerLabel, selectPlayerCB), playerInfo, attributesButton, seasonStatsButton, carrierStatsButton);

        super.setLeft(controlBox);
        super.setCenter(playerAttributesTA);
    }

    public void reload(PlayerInfo info) {
        selectPlayerCB.setValue(info);

        playerInfo.reload(info);

        playerAttributesTA.reload(info.getAttributes());

        seasonStatsTA.clear();
        seasonStatsTA.appendText("MATCH           |  PA |  AB |   R |   H |  2B |  3B |  HR |  SH |  SF |  BB |  HP |  SB |  CS |   K | RBI |   AVG |   SLG |  PO |   A |   E |    IP | \n");
        var player = AssociationManager.getInstance().getPlayerById(info.getPlayerId());
        if (player != null) {
            player.getStats().forEach(record -> renderStatsRecord(seasonStatsTA, record, false, null));
        }
        renderStatsRecord(seasonStatsTA, player.getSeasonTotal(), false, null);

        careerStatsTA.clear();
        careerStatsTA.appendText("SEASON |   G |  PA |  AB |   R |   H |  2B |  3B |  HR |  SH |  SF |  BB |  HP |  SB |  CS |   K | RBI |   AVG |   SLG |  PO |   A |   E |    IP | \n");
        renderStatsRecord(careerStatsTA, player.getSeasonTotal(), true, player.getStats().size());
        // TODO make it variable for each year yet to come + make total career count
    }

    @Override
    public void focusedTeamChanged(Team newlyFocusedTeam) {
        selectPlayerCB.setItems(FXCollections.observableList(newlyFocusedTeam.getPlayers()));
        selectPlayerCB.setValue(selectPlayerCB.getItems().get(0));
    }

    private void renderStatsRecord(TextArea target, PlayerStats record, boolean careerStats, Integer totalGames) {
        if (careerStats) {
            int year = AssociationManager.getInstance().getClock().getYear();
            target.appendText(StringUtils.rightPad(String.valueOf(year), 6, " ") + " | ");
            target.appendText(StringUtils.leftPad(String.valueOf(totalGames), 3) + " | ");
        } else {
            target.appendText(StringUtils.rightPad(record.getMatchStr(), 15, " ") + " | ");
        }
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBPA()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBAB()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBR()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBH()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getB2B()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getB3B()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBHR()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBSH()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBSF()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBBB()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBHP()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBSB()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBCS()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBK()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getBRB()), 3) + " | ");
        target.appendText(StatsUtils.getAVG(record.getBAB(), record.getBH()) + " | ");
        target.appendText(StatsUtils.getSLG(record.getBAB(), record.getBH(), record.getB2B(), record.getB3B(), record.getBHR()) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getFPO()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getFA()), 3) + " | ");
        target.appendText(StringUtils.leftPad(String.valueOf(record.getFE()), 3) + " | ");
        target.appendText(StringUtils.leftPad(StatsUtils.getIP(record.getFIP()), 5) + " | \n");
    }
}
