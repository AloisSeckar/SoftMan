package elrh.softman.gui.view.tab;

import elrh.softman.gui.tile.LineupTile;
import elrh.softman.logic.AssociationManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;

public class LineupTab extends AnchorPane {

    private final LineupTile lineupTile;
    private static LineupTab INSTANCE;

    public static LineupTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LineupTab();
        }
        return INSTANCE;
    }

    private LineupTab() {

        var playerTeam = AssociationManager.getInstance().getPlayerTeam();

        lineupTile = new LineupTile(playerTeam.getPlayers());
        super.getChildren().add(lineupTile);
        AnchorPane.setLeftAnchor(lineupTile, 10d);
        AnchorPane.setTopAnchor(lineupTile, 10d);

        var saveButton = new Button("Save lineup");
        saveButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> saveLineup());
        super.getChildren().add(saveButton);
        AnchorPane.setLeftAnchor(saveButton, 10d);
        AnchorPane.setTopAnchor(saveButton, 525d);

    }

    private void saveLineup() {
        String check = lineupTile.checkLineup();
        if (StringUtils.isBlank(check)) {

        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(check);
            alert.showAndWait();
        }
    }

}
