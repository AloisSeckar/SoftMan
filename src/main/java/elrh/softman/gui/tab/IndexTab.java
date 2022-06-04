package elrh.softman.gui.tab;

import elrh.softman.gui.table.LeagueStadingsTable;
import elrh.softman.gui.tile.ScheduleRowTile;
import elrh.softman.logic.AssociationManager;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class IndexTab extends BorderPane {

    private static IndexTab INSTANCE;

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

        dailySchedule = new VBox();
        super.setLeft(dailySchedule);

        leagueTable = new LeagueStadingsTable(AssociationManager.getInstance().getPlayerLeague().getStandings());
        super.setRight(leagueTable);

    }

    public void setDailySchedule() {
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
