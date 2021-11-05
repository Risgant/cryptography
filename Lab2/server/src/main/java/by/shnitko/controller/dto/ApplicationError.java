package by.shnitko.controller.dto;

import by.shnitko.error.ApplicationException;
import by.shnitko.util.DateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(value = AccessLevel.NONE)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ApplicationError {
    @DateTime @Builder.Default
    private Instant timestamp = Instant.now();
    private int status;
    private String path;
    private String error;
    private String message;
    private String code;

    public static ApplicationError of(ApplicationException exception, String path) {
        return ApplicationError.builder()
                .status(exception.getHttpStatus().value())
                .path(path)
                .error(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .code(exception.getHttpStatus().name())
                .build();
    }
}
