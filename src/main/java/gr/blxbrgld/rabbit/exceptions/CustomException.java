package gr.blxbrgld.rabbit.exceptions;

/**
 * Custom Exception Class
 * @author blxbrgld
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CustomException(){}

    public CustomException(final String message) {
        super(message);
    }

    public CustomException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CustomException(final Throwable cause) {
        super(cause);
    }
}
