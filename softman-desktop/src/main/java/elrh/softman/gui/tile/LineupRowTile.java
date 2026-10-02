package elrh.softman.gui.tile;

import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.logic.core.Lineup;
import elrh.softman.logic.core.data.PlayerInfo;
import elrh.softman.logic.core.data.PlayerRecord;
import elrh.softman.logic.enums.PlayerPosition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    public LineupRowTile(LineupTile parent, int row, boolean selectPosition) {
        this.row = row;
        this.selectPosition = selectPosition;

        super.setSpacing(Space.S.getPx());
        super.setAlignment(Pos.CENTER_LEFT);

        var rowLabel = new Label(StringUtils.leftPad(row + ".", 3, "0"));
        rowLabel.getStyleClass().addAll(Tokens.CAPTION, Tokens.MONO);
        rowLabel.setMinWidth(Layouts.em(2));
        super.getChildren().add(rowLabel);

        playerCB = new ComboBox<>();
        playerCB.getStyleClass().add(Tokens.READONLY);
        playerCB.setPrefWidth(Layouts.em(14));
        super.getChildren().add(playerCB);

        if (selectPosition) {
            positionCB = new ComboBox<>(FXCollections.observableList(PlayerPosition.getAvailablePositions(row < Lineup.POSITION_PLAYERS, false)));
            super.getChildren().add(positionCB);
        } else {
            positionCB = new ComboBox<>();
        }
        positionCB.getStyleClass().add(Tokens.READONLY);
        positionCB.setPrefWidth(Layouts.em(6));

        playerCB.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue == null) {
                positionCB.setValue(null);
            }
            if (row >= Lineup.POSITION_PLAYERS) {
                parent.handleFlexChange(newValue);
            }
        });
    }

    public void setUp(ObservableList<PlayerInfo> players, PlayerRecord current) {
        playerCB.setItems(players);

        if (current != null) {
            playerCB.setValue(current.getPlayer());
            if (selectPosition) {
                positionCB.setValue(current.getPosition());
            }
        } else {
            clear();
        }
    }

    public void clear() {
        playerCB.setValue(null);
        positionCB.setValue(null);
    }

    public void setReadOnly(boolean readOnly) {
        playerCB.setDisable(readOnly);
        positionCB.setDisable(readOnly);
    }

    public PlayerRecord getCurrentSelection() {
        return new PlayerRecord(playerCB.getValue(), positionCB.getValue());
    }

    public boolean isFilled() {
        return playerCB.getValue() != null && (positionCB.getValue() != null || !selectPosition);
    }

}
