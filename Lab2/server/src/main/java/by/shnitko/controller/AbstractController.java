package by.shnitko.controller;

import by.shnitko.controller.dto.ApplicationError;
import by.shnitko.error.ApplicationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

public class AbstractController {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationError> errorHandler(HttpServletRequest request, ApplicationException ex) {
        return ResponseEntity.status(ex.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApplicationError.of(ex, request.getRequestURI()));
    }

}
