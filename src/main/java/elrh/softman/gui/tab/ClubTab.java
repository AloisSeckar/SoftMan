package elrh.softman.gui.tab;

import elrh.softman.gui.table.LeagueStadingsTable;
import elrh.softman.gui.tile.CalendarTile;
import elrh.softman.gui.tile.ClubInfoTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.interfaces.IFocusedClubListener;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class ClubTab extends HBox implements IFocusedClubListener, IFocusedTeamListener {

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
        infoTile = new ClubInfoTile();
        infoTile.setAlignment(Pos.CENTER);

        var infoPanel = new Panel("Club info");
        infoPanel.setMinWidth(450);
        infoPanel.setMaxWidth(450);
        infoPanel.getStyleClass().add("panel-info");
        infoPanel.setBody(infoTile);

        leagueTable = new LeagueStadingsTable();
        leagueTable.setAlignment(Pos.CENTER_RIGHT);
        var standingsPanel = new Panel("Standings");
        standingsPanel.getStyleClass().add("panel-info");
        standingsPanel.setBody(leagueTable);

        calendarTile = new CalendarTile();
        calendarTile.setAlignment(Pos.CENTER_RIGHT);
        var schedulePanel = new Panel("Schedule");
        schedulePanel.getStyleClass().add("panel-info");
        schedulePanel.setBody(calendarTile);

        var contentColumn = new VBox(standingsPanel, schedulePanel);

        super.getChildren().add(infoPanel);
        super.getChildren().add(contentColumn);

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
