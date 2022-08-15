package elrh.softman.utils;

import java.util.Collection;

public class Utils {

    public static <T> boolean listNotEmpty(Collection<T> list) {
        return list != null && list.size() > 0;
    }

    public static <T> T  getFirstItem(Collection<T> list) {
        T ret = null;
        if (listNotEmpty(list)) {
            ret = list.stream().toList().get(0);
        }
        return ret;
    }

}
