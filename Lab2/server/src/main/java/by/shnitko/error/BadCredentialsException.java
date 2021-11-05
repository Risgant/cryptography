package by.shnitko.error;

import org.springframework.http.HttpStatus;

public class BadCredentialsException extends ApplicationException {
    public BadCredentialsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return  HttpStatus.BAD_REQUEST;
    }
}
