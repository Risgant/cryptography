package by.shnitko.configuration;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Validated
@ConfigurationProperties(prefix = "notepad")
public class AppProperties {
    @NotNull
    private DatabaseProps database;

    @Data
    @Validated
    @NoArgsConstructor
    public static class DatabaseProps {
        @NotBlank
        private String driverClassName;
        @NotBlank
        private String database;
        @NotBlank
        private String host;
        private Integer port;
        @NotBlank
        private String username;
        @NotBlank
        private String password;

        public String jdbcUrl() {
            return String.format("jdbc:postgresql://%s%s/%s",
                    host,
                    port == null ? "" : (":" + port),
                    database) ;
        }

    }
}
