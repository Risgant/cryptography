package by.shnitko.error;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApplicationException{

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
