package by.shnitko.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class NoteCreatingInfo {
    private UUID userId;
    private String text;
}
