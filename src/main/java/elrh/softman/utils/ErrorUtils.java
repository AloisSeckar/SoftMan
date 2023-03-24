package elrh.softman.utils;

import elrh.softman.logic.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class ErrorUtils {

    private ErrorUtils() {}

    public static void raise(String msg) throws AssertionError {
        LOG.error(msg);
        throw new AssertionError(msg);
    }

    public static Result handleException(String source, Throwable ex) {
        if (!(ex instanceof ReportedException)) {
            LOG.error(source, ex);
        }
        return new Result(false, ExceptionUtils.getRootCauseMessage(ex));
    }

    public static class ReportedException extends Exception {
        public ReportedException(String message) {
            super(message);
        }
    }
}
