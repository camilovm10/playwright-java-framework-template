package common.exceptions;

public class PageCreationException extends RuntimeException {

    public PageCreationException() {
    }

    public PageCreationException(String message) {
        super(message);
    }

    public PageCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageCreationException(Throwable cause) {
        super(cause);
    }

    public PageCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
