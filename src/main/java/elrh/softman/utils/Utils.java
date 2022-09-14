package elrh.softman.utils;

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

}
