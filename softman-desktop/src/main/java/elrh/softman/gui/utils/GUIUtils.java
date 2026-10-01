package elrh.softman.gui.utils;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class GUIUtils {

    public static Image getImageOrDefault(String src) {
        try {
            return new Image(GUIUtils.class.getResourceAsStream(src));
        } catch (NullPointerException nex) {
            // this image should always exist in resources
            return new Image(GUIUtils.class.getResourceAsStream("/img/ball.png"));
        }
    }

    public static void setBackgroundColor(Region region, Color color) {
        region.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public static void setBackgroundColor(Region region, String webColor) {
        setBackgroundColor(region, Color.web(webColor));
    }

}
