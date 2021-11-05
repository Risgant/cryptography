package by.shnitko.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class NoteUpdatingInfo {
    private UUID id;
    private String text;
}
