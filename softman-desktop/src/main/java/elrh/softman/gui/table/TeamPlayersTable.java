package elrh.softman.gui.table;

import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Tables;
import elrh.softman.gui.tab.PlayerTab;
import elrh.softman.gui.tile.PlayerInfoTile;
import elrh.softman.logic.core.data.PlayerAttributes;
import elrh.softman.logic.core.data.PlayerInfo;
import elrh.softman.utils.Utils;
import java.util.List;
import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class TeamPlayersTable extends VBox {

    private final TableView<PlayerInfo> table = Tables.table();
    private final ObservableList<PlayerInfo> data = FXCollections.observableArrayList();

    private PlayerInfoTile playerInfo;

    public TeamPlayersTable() {
        table.setItems(data);
        table.getColumns().setAll(List.of(
            Tables.column("#", PlayerInfo::getNumber).centered().width(3).build(),
            Tables.column("Name", PlayerInfo::getName).width(14).build(),
            Tables.column("Age", PlayerInfo::getAge).centered().width(4).build(),
            rating("Overall", PlayerAttributes::getTotal),
            rating("Batting", PlayerAttributes::getBattingSkill),
            rating("Pitching", PlayerAttributes::getPitchingSkill),
            rating("Fielding", PlayerAttributes::getFieldingSkill),
            rating("Physical", PlayerAttributes::getPhysicalSkill)));

        var selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        var selectedItems = selectionModel.getSelectedItems();
        selectedItems.addListener(
                (ListChangeListener<PlayerInfo>) change -> {
                    if (playerInfo != null && Utils.listNotEmpty(change.getList())) {
                        var selected = change.getList().get(0);
                        playerInfo.reload(selected);
                        PlayerTab.getInstance().reload(selected);
                    }
                }
        );

        getChildren().add(Layouts.grow(table));
    }

    public void reload(List<PlayerInfo> players) {
        data.clear();
        if (players != null) {
            data.addAll(players);
        }
        FXCollections.sort(data);
        table.refresh();
        if (Utils.listNotEmpty(data)) {
            table.getSelectionModel().select(0);
        }
    }

    public void setPlayerInfo(PlayerInfoTile playerDetail) {
        this.playerInfo = playerDetail;
    }

    private static TableColumn<PlayerInfo, Integer> rating(String title, Function<PlayerAttributes, Integer> attribute) {
        return Tables.<PlayerInfo, Integer>column(title, player -> attribute.apply(player.getAttributes())).rating().width(5.5).build();
    }
}
