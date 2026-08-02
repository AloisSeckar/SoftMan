package elrh.softman.gui.tile;

import static elrh.softman.logic.enums.PlayerPosition.*;

import elrh.softman.logic.db.orm.player.PlayerRecord;
import elrh.softman.logic.enums.PlayerPosition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class DefenseTile extends Pane {

    private static final int W2 = 425; // bg width  = 850px
    private static final int H2 = 300; // bg H2 = 600px

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

        posLF.setLayoutX(50);
        posLF.setLayoutY(H2);
        super.getChildren().add(posLF);

        posCF.setLayoutX(W2 - 100);
        posCF.setLayoutY(H2 - 30);
        super.getChildren().add(posCF);

        posRF.setLayoutX(W2 * 2 - 250);
        posRF.setLayoutY(H2);
        super.getChildren().add(posRF);

        posSS.setLayoutX(W2 - 225);
        posSS.setLayoutY(H2 + 70);
        super.getChildren().add(posSS);
        
        pos2B.setLayoutX(W2 + 25);
        pos2B.setLayoutY(H2 + 70);
        super.getChildren().add(pos2B);

        pos3B.setLayoutX(85);
        pos3B.setLayoutY(H2 + 120);
        super.getChildren().add(pos3B);

        posP.setLayoutX(W2 - 100);
        posP.setLayoutY(H2 + 145);
        super.getChildren().add(posP);

        pos1B.setLayoutX(W2 * 2 - 285);
        pos1B.setLayoutY(H2 + 120);
        super.getChildren().add(pos1B);

        posC.setLayoutX(W2 - 100);
        posC.setLayoutY(H2 + 240);
        super.getChildren().add(posC);

        posDP.setLayoutX(30);
        posDP.setLayoutY(H2 + 240);
        super.getChildren().add(posDP);
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
            default -> { /* nothing */ }
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
