package elrh.softman.gui.tab;

import elrh.softman.gui.table.TeamPlayersTable;
import elrh.softman.gui.tile.PlayerInfoTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.interfaces.IFocusedClubListener;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import elrh.softman.utils.Utils;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TeamTab extends AnchorPane implements IFocusedTeamListener, IFocusedClubListener {

    private final Label nameLabel;
    private final TeamPlayersTable playersTable;

    private static TeamTab INSTANCE;
    
    public static TeamTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TeamTab();
        }
        return INSTANCE;
    }
    
    private TeamTab() {

        nameLabel = new Label();
        nameLabel.getStyleClass().setAll("h3");
        super.getChildren().add(nameLabel);
        AnchorPane.setLeftAnchor(nameLabel, 10d);
        AnchorPane.setTopAnchor(nameLabel, 10d);

        playersTable = new TeamPlayersTable();
        super.getChildren().add(playersTable);
        AnchorPane.setLeftAnchor(playersTable, 10d);
        AnchorPane.setTopAnchor(playersTable, 40d);

        var playerInfo = new PlayerInfoTile(true);
        super.getChildren().add(playerInfo);
        AnchorPane.setRightAnchor(playerInfo, 10d);
        AnchorPane.setTopAnchor(playerInfo, 10d);
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

    private void reload(Team dispalayedTeam) {
        if (dispalayedTeam != null) {
            nameLabel.setText(dispalayedTeam.getName());
            playersTable.reload(dispalayedTeam.getPlayers());
        } else {
            nameLabel.setText("No team selected");
            playersTable.reload(null);
        }
    }

    @Override
    public void focusedClubChanged(Club newlyFocusedClub) {
        if (newlyFocusedClub != null) {
            Utils.setBackgroundColor(TeamTab.this, newlyFocusedClub.getColor());
        } else {
            Utils.setBackgroundColor(TeamTab.this, Color.GRAY);
        }
    }
}
