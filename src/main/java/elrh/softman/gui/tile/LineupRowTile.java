package elrh.softman.gui.tile;

import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.core.lineup.PlayerRecord;
import elrh.softman.logic.enums.PlayerPosition;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public class LineupRowTile extends HBox {

    private final ComboBox<PlayerInfo> playerCB;
    private final ComboBox<PlayerPosition> positionCB;

    @Getter
    private final int row;

    public LineupRowTile(int row) {
        this.row = row;

        super.setSpacing(10);
        super.setAlignment(Pos.CENTER);

        var rowLabel = new Label(StringUtils.leftPad((row + 1) + ".", 3, "0"));
        super.getChildren().add(rowLabel);

        playerCB = new ComboBox<>();
        super.getChildren().add(playerCB);

        positionCB = new ComboBox<>(FXCollections.observableList(PlayerPosition.getAvailablePositions()));
        if (row > 8) {
            positionCB.setDisable(true);
        }
        super.getChildren().add(positionCB);
    }

    public void setUp(List<PlayerInfo> players, PlayerRecord current) {
        playerCB.setItems(FXCollections.observableList(players));

        if (current != null) {
            playerCB.setValue(current.getPlayer());
            positionCB.setValue(current.getPosition());
        }
    }

    public void setReadOnly(boolean readOnly) {
        playerCB.setDisable(readOnly);
        playerCB.setStyle("-fx-opacity: 1");
        positionCB.setDisable(readOnly || row > 8);
        positionCB.setStyle("-fx-opacity: 1");
    }

    public PlayerRecord getCurrentSelection() {
        return new PlayerRecord(playerCB.getValue(), positionCB.getValue());
    }

    public boolean isFilled() {
        return playerCB.getValue() != null && positionCB.getValue() != null;
    }

}
