package elrh.softman.gui.view.tab;

import elrh.softman.gui.table.TeamPlayersTable;
import elrh.softman.logic.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class TeamTab extends AnchorPane {

    private static TeamTab INSTANCE;
    
    public static TeamTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TeamTab();
        }
        return INSTANCE;
    }
    
    private TeamTab() {
        
        var playerTeam = AssociationManager.getInstance().getPlayerTeam();

        var teamLabel = new Label(playerTeam.getName());
        teamLabel.getStyleClass().setAll("h3");
        super.getChildren().add(teamLabel);
        AnchorPane.setLeftAnchor(teamLabel, 10d);
        AnchorPane.setTopAnchor(teamLabel, 10d);

        var playersTable = new TeamPlayersTable(playerTeam.getPlayers());
        super.getChildren().add(playersTable);
        AnchorPane.setLeftAnchor(playersTable, 10d);
        AnchorPane.setTopAnchor(playersTable, 40d);
        
    }
    
}
