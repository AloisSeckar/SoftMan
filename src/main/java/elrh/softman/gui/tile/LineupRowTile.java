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
    private final boolean selectPosition;

    public LineupRowTile(int row, boolean selectPosition) {
        this.row = row;
        this.selectPosition = selectPosition;

        super.setSpacing(10);
        super.setAlignment(Pos.CENTER);

        var rowLabel = new Label(StringUtils.leftPad(row + ".", 3, "0"));
        super.getChildren().add(rowLabel);

        playerCB = new ComboBox<>();
        super.getChildren().add(playerCB);

        if (selectPosition) {
            positionCB = new ComboBox<>(FXCollections.observableList(PlayerPosition.getAvailablePositions(row < 10, false)));
            super.getChildren().add(positionCB);
        } else {
            positionCB = new ComboBox<>();
        }
    }

    public void setUp(List<PlayerInfo> players, PlayerRecord current) {
        playerCB.setItems(FXCollections.observableList(players));

        if (current != null) {
            playerCB.setValue(current.getPlayer());
            if (selectPosition) {
                positionCB.setValue(current.getPosition());
            }
        }
    }

    public void setReadOnly(boolean readOnly) {
        playerCB.setDisable(readOnly);
        playerCB.setStyle("-fx-opacity: 1");
        positionCB.setDisable(readOnly);
        positionCB.setStyle("-fx-opacity: 1");
    }

    public PlayerRecord getCurrentSelection() {
        return new PlayerRecord(playerCB.getValue(), positionCB.getValue());
    }

    public boolean isFilled() {
        return playerCB.getValue() != null && (positionCB.getValue() != null || !selectPosition);
    }

}
