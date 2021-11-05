package by.shnitko.repository.entity;

import by.shnitko.controller.dto.NoteCreatingInfo;
import by.shnitko.controller.dto.NoteUpdatingInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "note")
public class NoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private UUID userId;
    private String title;
    private String text;

    public static NoteEntity toInstance(NoteCreatingInfo creatingInfo) {
        return NoteEntity.builder()
                .userId(creatingInfo.getUserId())
                .text(creatingInfo.getText())
                .build();
    }

    public static NoteEntity toInstance(NoteEntity saved, NoteUpdatingInfo updatingInfo) {
        return NoteEntity.builder()
                .id(updatingInfo.getId())
                .userId(saved.getUserId())
                .text(updatingInfo.getText())
                .build();
    }
}
