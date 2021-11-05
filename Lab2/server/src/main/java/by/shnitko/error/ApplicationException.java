package by.shnitko.error;

import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected ApplicationException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
}
