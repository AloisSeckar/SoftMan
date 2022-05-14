package elrh.softman.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorUtils {

    private ErrorUtils() {}

    public static void raise(String msg) throws AssertionError {
        LOG.error(msg);
        throw new AssertionError(msg);
    }
}
