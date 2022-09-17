package elrh.softman.gui.tab;

import elrh.softman.gui.tile.DefenseTile;
import elrh.softman.gui.tile.LineupTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.core.lineup.Lineup;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;

public class LineupTab extends AnchorPane implements IFocusedTeamListener {

    private final LineupTile lineupTile;

    private final DefenseTile defenseTile;
    private static LineupTab INSTANCE;

    public static LineupTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LineupTab();
        }
        return INSTANCE;
    }

    private LineupTab() {

        var playerTeam = AssociationManager.getInstance().getUser().getActiveClub().getTeams().get(0);
        playerTeam.randomizeLineup();

        lineupTile = new LineupTile(false);
        lineupTile.fillLineup(playerTeam.getDefaultLineup());
        super.getChildren().add(lineupTile);
        AnchorPane.setLeftAnchor(lineupTile, 10d);
        AnchorPane.setTopAnchor(lineupTile, 10d);

        var saveButton = new Button("Save lineup");
        saveButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> saveLineup());
        super.getChildren().add(saveButton);
        AnchorPane.setLeftAnchor(saveButton, 10d);
        AnchorPane.setTopAnchor(saveButton, 525d);

        defenseTile = new DefenseTile();
        super.getChildren().add(defenseTile);
        AnchorPane.setRightAnchor(defenseTile, 10d);
        AnchorPane.setTopAnchor(defenseTile, 10d);

        AssociationManager.getInstance().getUser().registerFocusedTeamListener(this);
    }

    @Override
    public void focusedTeamChanged(Team newlyFocusedTeam) {
        // TODO adjustments after team changed
    }

    private void saveLineup() {
        String check = lineupTile.checkLineup();
        if (StringUtils.isBlank(check)) {
            Lineup lineup = lineupTile.getLineup();
            AssociationManager.getInstance().getUser().getActiveClub().getTeams().get(0).setLineup(lineup);
            for (int i = 1; i <= Lineup.POSITION_PLAYERS; i++) {
                var current = lineup.getCurrentBatter(i);
                if (current != null) {
                    defenseTile.setPosition(current);
                }
            }
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(check);
            alert.showAndWait();
        }
    }

}
