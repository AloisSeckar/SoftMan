package elrh.softman.gui.tile;

import static elrh.softman.logic.enums.PlayerPosition.*;

import elrh.softman.logic.db.orm.player.PlayerRecord;
import elrh.softman.logic.enums.PlayerPosition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DefenseTile extends VBox {

    private final Label posP = createLabel(PITCHER);
    private final Label posC = createLabel(CATCHER);
    private final Label pos1B = createLabel(FIRST_BASE);
    private final Label pos2B = createLabel(SECOND_BASE);
    private final Label pos3B = createLabel(THIRD_BASE);
    private final Label posSS = createLabel(SHORT_STOP);
    private final Label posLF = createLabel(LEFT_FIELD);
    private final Label posCF = createLabel(CENTER_FIELD);
    private final Label posRF = createLabel(RIGHT_FIELD);
    private final Label posDP = createLabel(DESIGNATED_PLAYER);

    public DefenseTile() {
        super.getStyleClass().add("framed");
        super.getStyleClass().add("field");
        super.setAlignment(Pos.CENTER);

        HBox row1 = new HBox(posCF);
        row1.setAlignment(Pos.CENTER);
        row1.getStyleClass().add("padding-5");
        super.getChildren().add(row1);
        HBox row2 = new HBox(posLF,posRF);
        row2.setAlignment(Pos.CENTER);
        row2.getStyleClass().add("padding-5");
        super.getChildren().add(row2);
        HBox row3 = new HBox(posSS,pos2B);
        row3.setAlignment(Pos.CENTER);
        row3.getStyleClass().add("padding-5");
        super.getChildren().add(row3);
        HBox row4 = new HBox(pos3B,posP,pos1B);
        row4.setAlignment(Pos.CENTER);
        row4.getStyleClass().add("padding-5");
        super.getChildren().add(row4);
        HBox row5 = new HBox(posC);
        row5.setAlignment(Pos.CENTER);
        row5.getStyleClass().add("padding-5");
        super.getChildren().add(row5);
        HBox row6 = new HBox(posDP);
        row6.setAlignment(Pos.CENTER);
        row6.getStyleClass().add("padding-5");
        super.getChildren().add(row6);
    }

    public void setPosition(PlayerRecord position) {
        String text = position.getPlayer().toString();
        switch (position.getPosition()) {
            case PITCHER -> posP.setText(text);
            case CATCHER -> posC.setText(text);
            case FIRST_BASE -> pos1B.setText(text);
            case SECOND_BASE -> pos2B.setText(text);
            case THIRD_BASE -> pos3B.setText(text);
            case SHORT_STOP -> posSS.setText(text);
            case LEFT_FIELD -> posLF.setText(text);
            case CENTER_FIELD -> posCF.setText(text);
            case RIGHT_FIELD -> posRF.setText(text);
            case DESIGNATED_PLAYER, OFFENSIVE_ONLY -> posDP.setText(text);
        }
    }

    private static Label createLabel(PlayerPosition pos) {
        Label ret = new Label(pos.toString());
        ret.setAlignment(Pos.CENTER);
        ret.getStyleClass().add("framed");
        ret.getStyleClass().add("field-position");
        ret.getStyleClass().add("padding-5");
        return ret;
    }

}
