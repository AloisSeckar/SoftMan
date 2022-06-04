package elrh.softman.gui.tab;

import elrh.softman.gui.table.LeagueStadingsTable;
import elrh.softman.gui.tile.CalendarTile;
import elrh.softman.logic.AssociationManager;
import javafx.scene.layout.BorderPane;

public class IndexTab extends BorderPane {

    private static IndexTab INSTANCE;

    private final CalendarTile calendarTile;
    private final LeagueStadingsTable leagueTable;

    public static IndexTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IndexTab();
        }
        return INSTANCE;
    }

    private IndexTab() {
        calendarTile = new CalendarTile();
        calendarTile.getStyleClass().add("padding-5");
        super.setLeft(calendarTile);

        leagueTable = new LeagueStadingsTable(AssociationManager.getInstance().getPlayerLeague().getStandings());
        leagueTable.getStyleClass().add("padding-5");
        super.setRight(leagueTable);
    }

    public void setDailySchedule() {
        calendarTile.setDailySchedule();
    }

    public void refreshSchedule() {
        calendarTile.refreshSchedule();
        leagueTable.refresh();
    }
}
