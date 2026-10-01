package elrh.softman.gui.tab;

import atlantafx.base.theme.Styles;
import elrh.softman.gui.frame.ContentFrame;
import elrh.softman.gui.frame.ContentFrame.Screen;
import elrh.softman.gui.kit.Cards;
import elrh.softman.gui.kit.Icons;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tables;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.tile.PlayerAttributesTile;
import elrh.softman.gui.tile.PlayerInfoTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.core.data.PlayerInfo;
import elrh.softman.logic.core.data.PlayerStats;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import elrh.softman.utils.StatsUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;

public class PlayerTab extends BorderPane implements IFocusedTeamListener {

    private static final String SEASON_TOTAL = "Season total";

    private final ComboBox<PlayerInfo> selectPlayerCB;
    private final ObservableList<PlayerInfo> data;
    private final PlayerInfoTile playerInfo = new PlayerInfoTile(false);
    private final PlayerAttributesTile playerAttributesTA = new PlayerAttributesTile();
    private final TableView<StatsRow> seasonStatsTable = statsTable("Match");
    private final TableView<StatsRow> careerStatsTable = statsTable("Season");

    // one stats table line; matchId is set for single-game rows only
    private record StatsRow(String label, UUID matchId, int games, PlayerStats stats) {
    }

    private static PlayerTab INSTANCE;
    public static PlayerTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerTab();
        }
        return INSTANCE;
    }

    private PlayerTab() {
        setPadding(Space.S.insets());
        data = FXCollections.observableList(new ArrayList<>());

        var selectPlayerLabel = new Label("Player");
        selectPlayerLabel.getStyleClass().add(Tokens.CAPTION);
        selectPlayerCB = new ComboBox<>(data);
        selectPlayerCB.valueProperty().addListener((ov, oldValue, newValue) -> reload(newValue));

        var controlBox = Layouts.column(Space.S, Layouts.row(Space.S, selectPlayerLabel, Layouts.grow(selectPlayerCB)), playerInfo);
        controlBox.setPrefWidth(Layouts.em(22));
        BorderPane.setMargin(controlBox, new Insets(0, Space.S.getPx(), 0, 0));

        var content = new StackPane();
        var group = new ToggleGroup();
        var attributesToggle = toggle(group, "Attributes", Feather.SLIDERS, Styles.LEFT_PILL, playerAttributesTA);
        toggle(group, "Season stats", Feather.CALENDAR, Styles.CENTER_PILL, Cards.titled("Season stats", seasonStatsTable));
        toggle(group, "Career stats", Feather.AWARD, Styles.RIGHT_PILL, Cards.titled("Career stats", careerStatsTable));
        group.selectedToggleProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue == null) {
                // keep one view selected when the active toggle is clicked again
                oldValue.setSelected(true);
            } else {
                content.getChildren().setAll((Node) newValue.getUserData());
            }
        });
        attributesToggle.setSelected(true);

        var switcher = new HBox();
        group.getToggles().forEach(toggle -> switcher.getChildren().add((ToggleButton) toggle));

        super.setLeft(controlBox);
        super.setCenter(Layouts.column(Space.S, switcher, Layouts.grow(content)));

        var user = AssociationManager.getInstance().getUser();
        focusedTeamChanged(user.getFocusedTeam());
        user.registerFocusedTeamListener(this);
    }

    public void reload(PlayerInfo info) {
        if (info != null) {
            selectPlayerCB.setValue(info);

            playerInfo.reload(info);

            playerAttributesTA.reload(info.getAttributes());

            var seasonRows = new ArrayList<StatsRow>();
            var careerRows = new ArrayList<StatsRow>();
            var player = AssociationManager.getInstance().getPlayerById(info.getPlayerId());
            if (player != null) {
                player.getStats().forEach(record -> seasonRows.add(seasonRow(record)));
                seasonRows.add(seasonRow(player.getSeasonTotal()));

                // TODO make it variable for each year yet to come + make total career count
                int year = AssociationManager.getInstance().getClock().getYear();
                careerRows.add(new StatsRow(String.valueOf(year), null, player.getStats().size(), player.getSeasonTotal()));
            }
            seasonStatsTable.getItems().setAll(seasonRows);
            careerStatsTable.getItems().setAll(careerRows);
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

    private static StatsRow seasonRow(PlayerStats record) {
        return SEASON_TOTAL.equals(record.getMatchStr())
            ? new StatsRow(record.getMatchStr(), null, record.getGames(), record)
            : new StatsRow(record.getMatchStr(), record.getMatchId(), 1, record);
    }

    private static ToggleButton toggle(ToggleGroup group, String text, Ikon icon, String pill, Node view) {
        var toggle = new ToggleButton(text, Icons.of(icon));
        toggle.getStyleClass().add(pill);
        toggle.setToggleGroup(group);
        toggle.setUserData(view);
        return toggle;
    }

    private static TableView<StatsRow> statsTable(String labelTitle) {
        var table = Tables.<StatsRow>table(Styles.DENSE);
        // too many columns to squeeze; scroll horizontally instead
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().setAll(List.of(
            Tables.<StatsRow, StatsRow>column(labelTitle, row -> row).width(12).sortable(false).cells(c -> new LabelCell()).build(),
            Tables.<StatsRow, Integer>column("G", StatsRow::games).numeric().width(3.5).build(),
            stat("PA", PlayerStats::getBPA),
            stat("AB", PlayerStats::getBAB),
            stat("R", PlayerStats::getBR),
            stat("H", PlayerStats::getBH),
            stat("2B", PlayerStats::getB2B),
            stat("3B", PlayerStats::getB3B),
            stat("HR", PlayerStats::getBHR),
            stat("SH", PlayerStats::getBSH),
            stat("SF", PlayerStats::getBSF),
            stat("BB", PlayerStats::getBBB),
            stat("HP", PlayerStats::getBHP),
            stat("SB", PlayerStats::getBSB),
            stat("CS", PlayerStats::getBCS),
            stat("K", PlayerStats::getBK),
            stat("RBI", PlayerStats::getBRB),
            text("AVG", s -> StatsUtils.getAVG(s.getBAB(), s.getBH())),
            text("SLG", s -> StatsUtils.getSLG(s.getBAB(), s.getBH(), s.getB2B(), s.getB3B(), s.getBHR())),
            stat("PO", PlayerStats::getFPO),
            stat("A", PlayerStats::getFA),
            stat("E", PlayerStats::getFE),
            text("IP", s -> StatsUtils.getIP(s.getFIP()))));
        return table;
    }

    private static TableColumn<StatsRow, Integer> stat(String title, ToIntFunction<PlayerStats> value) {
        return Tables.<StatsRow, Integer>column(title, row -> value.applyAsInt(row.stats())).numeric().width(3.5).build();
    }

    private static TableColumn<StatsRow, String> text(String title, Function<PlayerStats, String> value) {
        return Tables.<StatsRow, String>column(title, row -> value.apply(row.stats())).numeric().width(4.5).build();
    }

    private static void openMatch(UUID matchId) {
        MatchTab.getInstance().setMatch(Match.getMatchDetail(matchId));
        ContentFrame.getInstance().switchTo(Screen.MATCH);
    }

    // single-game rows link to the match, total rows are bold text
    private static final class LabelCell extends TableCell<StatsRow, StatsRow> {
        @Override
        protected void updateItem(StatsRow row, boolean empty) {
            super.updateItem(row, empty);
            setText(null);
            setGraphic(null);
            getStyleClass().remove(Styles.TEXT_BOLD);
            if (empty || row == null) {
                return;
            }
            if (row.matchId() != null) {
                var link = new Hyperlink(row.label());
                link.setOnAction(e -> openMatch(row.matchId()));
                setGraphic(link);
            } else {
                setText(row.label());
                getStyleClass().add(Styles.TEXT_BOLD);
            }
        }
    }
}
