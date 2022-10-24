package elrh.softman.gui.tab;

import elrh.softman.gui.frame.ContentFrame;
import elrh.softman.gui.tile.PlayerAttributesTile;
import elrh.softman.gui.tile.PlayerInfoTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.db.orm.player.PlayerInfo;
import elrh.softman.logic.db.orm.player.PlayerStats;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import elrh.softman.utils.FormatUtils;
import elrh.softman.utils.StatsUtils;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.commons.lang3.StringUtils;

public class PlayerTab extends BorderPane implements IFocusedTeamListener {

    private final ComboBox<PlayerInfo> selectPlayerCB;
    private final ObservableList<PlayerInfo> data;
    private final PlayerInfoTile playerInfo = new PlayerInfoTile(false);
    private final PlayerAttributesTile playerAttributesTA = new PlayerAttributesTile();
    private final TextFlow seasonStatsTA = new TextFlow();
    private final TextFlow careerStatsTA = new TextFlow();

    private static PlayerTab INSTANCE;
    public static PlayerTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerTab();
        }
        return INSTANCE;
    }

    private PlayerTab() {
        data = FXCollections.observableList(new ArrayList<>());

        var selectPlayerLabel = new Label(" Select player: ");
        selectPlayerCB = new ComboBox<>(data);
        selectPlayerCB.setMinWidth(220d);
        selectPlayerCB.setMaxWidth(220d);
        selectPlayerCB.valueProperty().addListener((ov, oldValue, newValue) -> reload(newValue));

        seasonStatsTA.getStyleClass().add("font-courier-12");
        seasonStatsTA.setPadding(FormatUtils.PADDING_5);
        careerStatsTA.getStyleClass().add("font-courier-12");
        careerStatsTA.setPadding(FormatUtils.PADDING_5);

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

        var user = AssociationManager.getInstance().getUser();
        focusedTeamChanged(user.getFocusedTeam());
        user.registerFocusedTeamListener(this);
    }

    public void reload(PlayerInfo info) {
        if (info != null) {
            selectPlayerCB.setValue(info);

            playerInfo.reload(info);

            playerAttributesTA.reload(info.getAttributes());

            seasonStatsTA.getChildren().clear();
            var sHeader = new Text("MATCH           |  PA |  AB |   R |   H |  2B |  3B |  HR |  SH |  SF |  BB |  HP |  SB |  CS |   K | RBI |   AVG |   SLG |  PO |   A |   E |    IP | \n");
            seasonStatsTA.getChildren().add(sHeader);
            var player = AssociationManager.getInstance().getPlayerById(info.getPlayerId());
            if (player != null) {
                player.getStats().forEach(record -> renderStatsRecord(seasonStatsTA, record, false, null));
                renderStatsRecord(seasonStatsTA, player.getSeasonTotal(), false, null);
            }

            careerStatsTA.getChildren().clear();
            var cHeader = new Text("SEASON |   G |  PA |  AB |   R |   H |  2B |  3B |  HR |  SH |  SF |  BB |  HP |  SB |  CS |   K | RBI |   AVG |   SLG |  PO |   A |   E |    IP | \n");
            careerStatsTA.getChildren().add(cHeader);
            if (player != null) {
                renderStatsRecord(careerStatsTA, player.getSeasonTotal(), true, player.getStats().size());
            }
            // TODO make it variable for each year yet to come + make total career count
        }
    }

    @Override
    public void focusedTeamChanged(Team newlyFocusedTeam) {
        data.clear();
        var players = newlyFocusedTeam.getPlayers();
        if (players != null) {
            data.addAll(players);
        }
        FXCollections.sort(data);

        selectPlayerCB.setValue(selectPlayerCB.getItems().get(0));
    }

    private void renderStatsRecord(TextFlow target, PlayerStats record, boolean careerStats, Integer totalGames) {
        if (careerStats) {
            int year = AssociationManager.getInstance().getClock().getYear();
            target.getChildren().add(new Text(StringUtils.rightPad(String.valueOf(year), 6, " ") + " | "));
            target.getChildren().add(new Text(StringUtils.leftPad(String.valueOf(totalGames), 3) + " | "));
        } else {
            if ("Season total".equals(record.getMatchStr())) {
                target.getChildren().add(new Text(StringUtils.rightPad(record.getMatchStr(), 15, " ") + " | "));
            } else {
                var matchLink = new Hyperlink(StringUtils.rightPad(record.getMatchStr(), 15, " "));
                matchLink.setBorder(Border.EMPTY);
                matchLink.setPadding(new Insets(4, 0, 4, 0));
                matchLink.setOnAction(e -> {
                    var match = Match.getMatchDetail(record.getMatchId());
                    MatchTab.getInstance().setMatch(match);
                    ContentFrame.getInstance().switchTo("Match");
                });
                target.getChildren().add(matchLink);
                target.getChildren().add(new Text(" | "));
            }
        }

        var sb = new StringBuilder();
        sb.append(StringUtils.leftPad(String.valueOf(record.getBPA()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBAB()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBR()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBH()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getB2B()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getB3B()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBHR()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBSH()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBSF()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBBB()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBHP()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBSB()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBCS()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBK()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getBRB()), 3)).append(" | ");
        sb.append(StatsUtils.getAVG(record.getBAB(), record.getBH())).append(" | ");
        sb.append(StatsUtils.getSLG(record.getBAB(), record.getBH(), record.getB2B(), record.getB3B(), record.getBHR())).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getFPO()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getFA()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(String.valueOf(record.getFE()), 3)).append(" | ");
        sb.append(StringUtils.leftPad(StatsUtils.getIP(record.getFIP()), 5)).append(" | \n");
        target.getChildren().add(new Text(sb.toString()));

    }
}
