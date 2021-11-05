package by.shnitko.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreatingInfo {
    private String name;
    private String password;
}
