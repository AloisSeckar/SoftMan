package elrh.softman.gui.kit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Layouts {

    @Getter
    @RequiredArgsConstructor
    public enum Space {
        XS(4), S(8), M(12), L(16), XL(24);

        private final double px;

        public Insets insets() {
            return new Insets(px);
        }
    }

    public static HBox row(Space gap, Node... children) {
        var row = new HBox(gap.getPx(), children);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    public static VBox column(Space gap, Node... children) {
        return new VBox(gap.getPx(), children);
    }

    public static Region spacer() {
        return grow(new Region());
    }

    // lets the node take all free space in HBox, VBox and GridPane parents
    public static <T extends Node> T grow(T node) {
        HBox.setHgrow(node, Priority.ALWAYS);
        VBox.setVgrow(node, Priority.ALWAYS);
        GridPane.setHgrow(node, Priority.ALWAYS);
        GridPane.setVgrow(node, Priority.ALWAYS);
        if (node instanceof Region region) {
            region.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        }
        return node;
    }

    public static <T extends Region> T padded(T region, Space space) {
        region.setPadding(space.insets());
        return region;
    }

    // AtlantaFX sets the root font to 14px; Font.getDefault() reports the platform size instead
    private static final double THEME_FONT_SIZE = 14;

    // font-relative size, so dimensions scale with the theme font instead of fixed px
    public static double em(double ems) {
        return THEME_FONT_SIZE * ems;
    }
}
