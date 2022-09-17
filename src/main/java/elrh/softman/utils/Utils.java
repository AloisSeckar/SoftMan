package elrh.softman.utils;

import javafx.scene.control.Label;

import java.util.List;

public class Utils {

    public static <T> boolean listNotEmpty(List<T> list) {
        return list != null && list.size() > 0 && list.get(0) != null;
    }

    public static <T> T  getFirstItem(List<T> list) {
        T ret = null;
        if (listNotEmpty(list)) {
            ret = list.get(0);
        }
        return ret;
    }

    // TODO why GUI content overflows?
    public static Label createPadding(double width) {
        var paddingLabel = new Label("");
        paddingLabel.setMaxWidth(width);
        paddingLabel.setMinWidth(width);
        return paddingLabel;
    }

}
