package elrh.softman.utils;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GUIUtils {

    public static Gauge getGauge(int size, Color color, String title) {
        return getGauge(Gauge.SkinType.FLAT, size, color, title);
    }

    public static Gauge getGauge(Gauge.SkinType type, int size, Color color, String title) {
        return GaugeBuilder.create()
                .skinType(type)
                .minValue(0).maxValue(100).decimals(0)
                .minWidth(size + 10).maxWidth(size + 10)
                .barColor(color).title(title)
                .build();
    }

    public static Image getImageOrDefault(String src) {
        try {
            return new Image(GUIUtils.class.getResourceAsStream(src));
        } catch (NullPointerException nex) {
            // this image should always exist in resources
            return new Image(GUIUtils.class.getResourceAsStream("/img/ball.png"));
        }
    }

}
