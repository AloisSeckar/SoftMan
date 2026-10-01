package elrh.softman.gui.tab;

import elrh.softman.gui.kit.Cards;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.table.TeamPlayersTable;
import elrh.softman.gui.tile.PlayerInfoTile;
import elrh.softman.gui.utils.GUIUtils;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.interfaces.IFocusedClubListener;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class TeamTab extends GridPane implements IFocusedTeamListener, IFocusedClubListener {

    private final Label nameLabel = Cards.title("");
    private final Region clubStripe = new Region();
    private final TeamPlayersTable playersTable;

    private static TeamTab INSTANCE;
    
    public static TeamTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TeamTab();
        }
        return INSTANCE;
    }
    
    private TeamTab() {
        setPadding(Space.S.insets());
        setHgap(Space.S.getPx());
        setVgap(Space.S.getPx());

        var column1 = new ColumnConstraints();
        column1.setPercentWidth(70);
        var column2 = new ColumnConstraints();
        column2.setPercentWidth(30);
        getColumnConstraints().addAll(column1, column2);

        clubStripe.getStyleClass().add(Tokens.CLUB_STRIPE);
        add(clubStripe, 0, 0, 2, 1);

        playersTable = new TeamPlayersTable();
        var roster = Cards.plain(playersTable);
        roster.setHeader(nameLabel);
        add(Layouts.grow(roster), 0, 1);

        var playerInfo = new PlayerInfoTile(true);
        playerInfo.setMaxHeight(Region.USE_PREF_SIZE);
        GridPane.setValignment(playerInfo, VPos.TOP);
        add(playerInfo, 1, 1);
        playersTable.setPlayerInfo(playerInfo);

        var user = AssociationManager.getInstance().getUser();

        focusedClubChanged(user.getFocusedClub());
        focusedTeamChanged(user.getFocusedTeam());

        user.registerFocusedClubListener(this);
        user.registerFocusedTeamListener(this);
    }

    @Override
    public void focusedTeamChanged(Team newlyFocusedTeam) {
        reload(newlyFocusedTeam);
    }

    private void reload(Team displayedTeam) {
        if (displayedTeam != null) {
            nameLabel.setText(displayedTeam.getName());
            playersTable.reload(displayedTeam.getPlayers());
        } else {
            nameLabel.setText("No team selected");
            playersTable.reload(null);
        }
    }

    @Override
    public void focusedClubChanged(Club newlyFocusedClub) {
        if (newlyFocusedClub != null) {
            GUIUtils.setBackgroundColor(clubStripe, newlyFocusedClub.getColor());
        } else {
            clubStripe.setBackground(null);
        }
    }
}
