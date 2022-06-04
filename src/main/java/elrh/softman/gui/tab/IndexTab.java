package elrh.softman.gui.tab;

import elrh.softman.gui.table.LeagueStadingsTable;
import elrh.softman.gui.tile.ScheduleRowTile;
import elrh.softman.logic.AssociationManager;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.time.LocalDate;
import java.util.ArrayList;

import elrh.softman.utils.FormatUtils;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class IndexTab extends BorderPane {

    private static IndexTab INSTANCE;

    private final Label titleLabel;
    private final VBox dailySchedule;
    private final ArrayList<ScheduleRowTile> dailyScheduleRows = new ArrayList<>();
    private final LeagueStadingsTable leagueTable;

    public static IndexTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IndexTab();
        }
        return INSTANCE;
    }

    private IndexTab() {

        titleLabel = new Label("Today");
        titleLabel.getStyleClass().setAll("h3");
        titleLabel.getStyleClass().add("padding-5");
        super.setTop(titleLabel);

        dailySchedule = new VBox();
        dailySchedule.getStyleClass().add("padding-5");
        super.setLeft(dailySchedule);

        leagueTable = new LeagueStadingsTable(AssociationManager.getInstance().getPlayerLeague().getStandings());
        leagueTable.getStyleClass().add("padding-5");
        super.setRight(leagueTable);

    }

    public void setDailySchedule() {
        var date = AssociationManager.getInstance().getCurrentDate();
        if (date != null) {
            titleLabel.setText(date.format(FormatUtils.DF));
        } else {
            titleLabel.setText("UNDEFINED");
        }

        dailySchedule.getChildren().clear();
        dailyScheduleRows.clear();
        var matches = AssociationManager.getInstance().getTodayMatches();
        int i = 0;
        for (var match : matches) {
            var row = new ScheduleRowTile(i++ % 2 == 0);
            row.setMatch(match);
            dailySchedule.getChildren().add(row);
            dailyScheduleRows.add(row);
        }
    }

    public void refreshSchedule() {
        for (var row : dailyScheduleRows) {
            row.refreshMatch();
        }
        leagueTable.refresh();
    }
}
