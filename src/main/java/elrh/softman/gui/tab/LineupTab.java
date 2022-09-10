package elrh.softman.gui.tab;

import elrh.softman.gui.tile.DefenseTile;
import elrh.softman.gui.tile.LineupTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.core.lineup.LineupPosition;
import elrh.softman.logic.interfaces.IFocusListener;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class LineupTab extends AnchorPane implements IFocusListener {

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

        var playerTeam = AssociationManager.getInstance().getState().getActiveClub().getTeams().get(0);
        playerTeam.randomizeLineup();

        lineupTile = new LineupTile(false);
        lineupTile.fillLineup(playerTeam);
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

    private void saveLineup() {
        String check = lineupTile.checkLineup();
        if (StringUtils.isBlank(check)) {
            List<LineupPosition> lineup = lineupTile.getLineup();
            lineup.forEach(defenseTile::setPosition);
            AssociationManager.getInstance().getState().getActiveClub().getTeams().get(0).setLineup(lineup);
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(check);
            alert.showAndWait();
        }
    }

}
