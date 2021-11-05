package by.shnitko.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotesDto {
    private List<NoteDto> notes;
    private List<String> keys;
    private String initVector;
}
