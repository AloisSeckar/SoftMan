package elrh.softman.gui.tab;

import elrh.softman.gui.table.LeagueStadingsTable;
import elrh.softman.gui.tile.CalendarTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.interfaces.IFocusListener;
import elrh.softman.utils.Constants;
import javafx.scene.layout.BorderPane;

public class IndexTab extends BorderPane implements IFocusListener {

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

        // TODO change dynamically according to User selection
        leagueTable = new LeagueStadingsTable(AssociationManager.getInstance().getLeagues(Constants.START_YEAR).get(0).getStandings());
        leagueTable.getStyleClass().add("padding-5");
        super.setRight(leagueTable);

        AssociationManager.getInstance().getState().registerFocusListener(this);

    }

    @Override
    public void focusedClubChanged(Club newlyFocusedClub) {
        // TODO adjustments after club changed
    }

    @Override
    public void focusedTeamChanged(Team newlyFocusedTeam) {
        // TODO adjustments after team changed
    }

    public void setDailySchedule() {
        calendarTile.setDailySchedule();
    }

    public void refreshSchedule() {
        calendarTile.refreshSchedule();
        leagueTable.refresh();
    }
}
