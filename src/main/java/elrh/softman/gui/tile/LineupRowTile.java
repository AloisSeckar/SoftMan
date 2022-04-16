package elrh.softman.gui.tile;

import elrh.softman.db.orm.PlayerInfo;
import elrh.softman.logic.LineupPosition;
import elrh.softman.logic.Position;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class LineupRowTile extends HBox {

    private final int row;

    private final ComboBox<PlayerInfo> playerCB;
    private final ComboBox<Position> positionCB;

    public LineupRowTile(int row, List<PlayerInfo> players) {
        this.row = row;

        super.setSpacing(10);
        super.setAlignment(Pos.CENTER);

        var rowLabel = new Label(StringUtils.leftPad(row + ".", 3, "0"));
        super.getChildren().add(rowLabel);

        playerCB = new ComboBox<>(FXCollections.observableList(players));
        super.getChildren().add(playerCB);

        positionCB = new ComboBox<>(FXCollections.observableList(Position.getAvailablePositions()));
        if (row > 9) {
            positionCB.setDisable(true);
        }
        super.getChildren().add(positionCB);
    }

    public LineupPosition getCurrentSelection() {
        return new LineupPosition(playerCB.getValue(), positionCB.getValue());
    }

}
