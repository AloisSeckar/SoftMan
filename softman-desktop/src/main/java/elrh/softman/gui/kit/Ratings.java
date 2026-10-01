package elrh.softman.gui.kit;

import elrh.softman.gui.kit.Layouts.Space;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Ratings {

    public static Bar bar(String label) {
        return new Bar(label);
    }

    public static VBox group(String title, Bar... bars) {
        var heading = new Label(title);
        heading.getStyleClass().add(Tokens.TITLE);
        var group = Layouts.column(Space.S, heading);
        group.getChildren().addAll(bars);
        return group;
    }

    // flat 0-100 attribute bar, coloured by tier
    public static final class Bar extends HBox {

        private final ProgressBar progress = new ProgressBar(0);
        private final Label value = new Label();

        private Bar(String label) {
            super(Space.S.getPx());
            getStyleClass().add(Tokens.RATING);
            setAlignment(Pos.CENTER_LEFT);

            var name = new Label(label);
            name.setMinWidth(Layouts.em(7));
            value.getStyleClass().add(Tokens.RATING_VALUE);
            value.setMinWidth(Layouts.em(2.5));

            getChildren().addAll(name, Layouts.grow(progress), value);
        }

        public void setValue(int rating) {
            int clamped = Math.clamp(rating, 0, 100);
            progress.setProgress(clamped / 100d);
            value.setText(String.valueOf(clamped));
            progress.getStyleClass().removeAll(Tokens.RATING_TIERS);
            progress.getStyleClass().add(tier(clamped));
        }

        private static String tier(int rating) {
            if (rating < 40) {
                return Tokens.RATING_LOW;
            } else if (rating < 60) {
                return Tokens.RATING_MID;
            } else if (rating < 80) {
                return Tokens.RATING_GOOD;
            }
            return Tokens.RATING_ELITE;
        }
    }
}
