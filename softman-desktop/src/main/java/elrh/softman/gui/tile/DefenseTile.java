package elrh.softman.gui.tile;

import static elrh.softman.logic.enums.PlayerPosition.*;

import atlantafx.base.controls.Card;
import elrh.softman.gui.kit.Cards;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.utils.GUIUtils;
import elrh.softman.logic.core.data.PlayerRecord;
import elrh.softman.logic.enums.PlayerPosition;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DefenseTile extends Card {

    private final Map<PlayerPosition, Label> labels = new EnumMap<>(PlayerPosition.class);

    public DefenseTile() {
        var field = new FieldPane();
        // marker centres as fractions of the field image
        place(field, LEFT_FIELD, 0.18, 0.52);
        place(field, CENTER_FIELD, 0.50, 0.47);
        place(field, RIGHT_FIELD, 0.82, 0.52);
        place(field, SHORT_STOP, 0.35, 0.64);
        place(field, SECOND_BASE, 0.65, 0.64);
        place(field, THIRD_BASE, 0.22, 0.72);
        place(field, PITCHER, 0.50, 0.77);
        place(field, FIRST_BASE, 0.78, 0.72);
        place(field, CATCHER, 0.50, 0.93);
        place(field, DESIGNATED_PLAYER, 0.15, 0.93);

        setHeader(Cards.title("Defense"));
        setBody(field);
    }

    public void setPosition(PlayerRecord position) {
        var key = position.getPosition() == OFFENSIVE_ONLY ? DESIGNATED_PLAYER : position.getPosition();
        var label = labels.get(key);
        if (label != null) {
            label.setText(position.getPlayer().toString());
        }
    }

    private void place(FieldPane field, PlayerPosition pos, double x, double y) {
        var label = new Label(pos.toString());
        label.setAlignment(Pos.CENTER);
        label.getStyleClass().add(Tokens.FIELD_POSITION);
        labels.put(pos, label);
        field.place(label, x, y);
    }

    // scales the field image with its aspect ratio and keeps markers on their fractional spots
    private static final class FieldPane extends Pane {

        private static final double MAX_LABEL_WIDTH = 0.24;

        private final ImageView image = new ImageView(GUIUtils.getImageOrDefault("/img/vecteezy/field.png"));
        private final Map<Label, Point2D> anchors = new LinkedHashMap<>();

        private FieldPane() {
            image.setPreserveRatio(true);
            image.setSmooth(true);
            getChildren().add(image);
            setPrefSize(Layouts.em(40), Layouts.em(28));
            setMinSize(Layouts.em(20), Layouts.em(14));
        }

        private void place(Label label, double x, double y) {
            anchors.put(label, new Point2D(x, y));
            getChildren().add(label);
        }

        @Override
        protected void layoutChildren() {
            var img = image.getImage();
            double ratio = img.getWidth() / img.getHeight();
            double width = Math.min(getWidth(), getHeight() * ratio);
            double height = width / ratio;
            double left = (getWidth() - width) / 2;
            double top = (getHeight() - height) / 2;

            image.setFitWidth(width);
            image.relocate(left, top);

            anchors.forEach((label, at) -> {
                double labelWidth = Math.min(label.prefWidth(-1), width * MAX_LABEL_WIDTH);
                double labelHeight = label.prefHeight(labelWidth);
                label.resizeRelocate(
                    left + at.getX() * width - labelWidth / 2,
                    top + at.getY() * height - labelHeight / 2,
                    labelWidth, labelHeight);
            });
        }
    }

}
