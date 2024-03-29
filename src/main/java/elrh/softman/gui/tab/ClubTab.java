package elrh.softman.gui.tab;

import elrh.softman.gui.table.LeagueStadingsTable;
import elrh.softman.gui.tile.CalendarTile;
import elrh.softman.gui.tile.ClubHeaderTile;
import elrh.softman.gui.tile.ClubInfoTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.interfaces.IFocusedClubListener;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class ClubTab extends GridPane implements IFocusedClubListener, IFocusedTeamListener {

    private static ClubTab INSTANCE;

    private final ClubInfoTile infoTile;
    private final CalendarTile calendarTile;
    private final LeagueStadingsTable leagueTable;

    public static ClubTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClubTab();
        }
        return INSTANCE;
    }

    private ClubTab() {
        var col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        var col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        super.getColumnConstraints().addAll(col1, col2);

        ClubHeaderTile headerTile = new ClubHeaderTile();
        headerTile.getStyleClass().add("padding-5");
        headerTile.setAlignment(Pos.CENTER);
        super.add(headerTile, 0, 0);

        infoTile = new ClubInfoTile();
        infoTile.getStyleClass().add("padding-5");
        infoTile.setAlignment(Pos.CENTER);
        super.add(infoTile, 0, 1);

        leagueTable = new LeagueStadingsTable();
        leagueTable.getStyleClass().add("padding-5");
        leagueTable.setAlignment(Pos.CENTER_RIGHT);
        super.add(leagueTable, 1, 0);

        calendarTile = new CalendarTile();
        calendarTile.getStyleClass().add("padding-5");
        calendarTile.setAlignment(Pos.CENTER_RIGHT);
        super.add(calendarTile, 1, 1);

        infoTile.reload(AssociationManager.getInstance().getUser().getFocusedClub());
        leagueTable.setLeague(AssociationManager.getInstance().getUser().getFocusedLeague());

        AssociationManager.getInstance().getUser().registerFocusedClubListener(this);
        AssociationManager.getInstance().getUser().registerFocusedTeamListener(this);

    }

    @Override
    public void focusedClubChanged(Club newlyFocusedClub) {
        infoTile.reload(newlyFocusedClub);
    }

    @Override
    public void focusedTeamChanged(Team newlyFocusedTeam) {
        leagueTable.setLeague(AssociationManager.getInstance().getUser().getFocusedLeague());
        setDailySchedule();
    }

    public void setDailySchedule() {
        var league = AssociationManager.getInstance().getUser().getFocusedLeague();
        var leagueId = league != null ? league.getId() : 0;
        calendarTile.setDailySchedule(leagueId);
        leagueTable.refresh();
    }

    public void refreshSchedule() {
        calendarTile.refreshSchedule();
        leagueTable.refresh();
    }
}
