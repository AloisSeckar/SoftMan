package elrh.softman.gui.tab;

import elrh.softman.gui.kit.Cards;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.table.LeagueStadingsTable;
import elrh.softman.gui.tile.CalendarTile;
import elrh.softman.gui.tile.ClubInfoTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.interfaces.IFocusedClubListener;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

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
        setPadding(Space.S.insets());
        setHgap(Space.S.getPx());
        setVgap(Space.S.getPx());

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70);
        getColumnConstraints().addAll(column1, column2);

        infoTile = new ClubInfoTile();
        infoTile.setMaxHeight(Region.USE_PREF_SIZE);
        GridPane.setValignment(infoTile, VPos.TOP);
        add(infoTile, 0, 0, 1, 2);

        leagueTable = new LeagueStadingsTable();
        add(Cards.titled("Standings", leagueTable), 1, 0);

        calendarTile = new CalendarTile();
        add(calendarTile, 1, 1);

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
        var leagueId = league != null ? league.getId() : null;
        calendarTile.setDailySchedule(leagueId);
        leagueTable.refresh();
    }

    public void refreshSchedule() {
        calendarTile.refreshSchedule();
        leagueTable.refresh();
    }
}
