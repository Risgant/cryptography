package by.shnitko.repository;

import by.shnitko.repository.entity.NoteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends CrudRepository<NoteEntity, UUID> {
    List<NoteEntity> findAllByUserId(UUID userId);
}
