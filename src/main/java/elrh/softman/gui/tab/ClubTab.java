package elrh.softman.gui.tab;

import elrh.softman.gui.table.LeagueStadingsTable;
import elrh.softman.gui.tile.CalendarTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.interfaces.IFocusListener;
import javafx.scene.layout.BorderPane;

public class ClubTab extends BorderPane implements IFocusListener {

    private static ClubTab INSTANCE;

    private final CalendarTile calendarTile;
    private final LeagueStadingsTable leagueTable;

    public static ClubTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClubTab();
        }
        return INSTANCE;
    }

    private ClubTab() {
        calendarTile = new CalendarTile();
        calendarTile.getStyleClass().add("padding-5");
        super.setLeft(calendarTile);

        leagueTable = new LeagueStadingsTable();
        leagueTable.getStyleClass().add("padding-5");
        super.setRight(leagueTable);

        leagueTable.setLeague(AssociationManager.getInstance().getState().getFocusedLeague());

        AssociationManager.getInstance().getState().registerFocusListener(this);

    }

    @Override
    public void focusedClubChanged(Club newlyFocusedClub) {
        // TODO adjustments after club changed
    }

    @Override
    public void focusedTeamChanged(Team newlyFocusedTeam) {
        leagueTable.setLeague(AssociationManager.getInstance().getState().getFocusedLeague());
    }

    public void setDailySchedule() {
        calendarTile.setDailySchedule();
    }

    public void refreshSchedule() {
        calendarTile.refreshSchedule();
        leagueTable.refresh();
    }
}
