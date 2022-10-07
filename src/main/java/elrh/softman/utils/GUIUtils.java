package elrh.softman.utils;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;

public class GUIUtils {

    public static Gauge getGauge(int size, Color color, String title) {
        return GaugeBuilder.create()
                .skinType(Gauge.SkinType.FLAT)
                        .minValue(0).maxValue(100).decimals(0)
                        .minWidth(size + 10).maxWidth(size + 10)
                        .barColor(color)
                        .title(title)
                        .build();
    }

}
