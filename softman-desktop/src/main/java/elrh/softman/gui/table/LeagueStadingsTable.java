package elrh.softman.gui.table;

import atlantafx.base.theme.Styles;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tables;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.logic.core.League;
import elrh.softman.logic.core.stats.Standing;
import java.util.List;
import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class LeagueStadingsTable extends VBox {

    private final Label nameLabel = new Label();
    private final TableView<Standing> table = Tables.table();
    private final ObservableList<Standing> data = FXCollections.observableArrayList();

    public LeagueStadingsTable() {
        super(Space.S.getPx());
        nameLabel.getStyleClass().add(Tokens.CAPTION);

        table.setItems(data);
        table.getColumns().setAll(List.of(
            Tables.<Standing, Integer>column("#", s -> data.indexOf(s) + 1).centered().width(3).sortable(false).build(),
            Tables.column("Team", Standing::getTeam).width(14).sortable(false).build(),
            stat("G", Standing::getGames),
            stat("W", Standing::getWins),
            stat("L", Standing::getLoses),
            stat("RF", Standing::getRunsFor),
            stat("RA", Standing::getRunsAgainst),
            Tables.column("Pts", Standing::getPoints).centered().width(4).style(Styles.TEXT_BOLD).build()));
        Tables.fitRows(table);

        getChildren().addAll(nameLabel, table);
    }

    public void setLeague(League league) {
        if (data.size() > 0) {
            data.clear();
        }
        if (league != null) {
            nameLabel.setText(league.getName());
            data.addAll(league.getStandings());
        } else {
            nameLabel.setText("Not participating in any league");
        }
        refresh();
    }

    public void refresh() {
        FXCollections.sort(data);
        table.refresh();
    }

    private static TableColumn<Standing, Integer> stat(String title, Function<Standing, Integer> value) {
        return Tables.column(title, value).centered().width(3.5).build();
    }
}
