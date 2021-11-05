package by.shnitko.service;

import by.shnitko.controller.dto.NoteCreatingInfo;
import by.shnitko.controller.dto.NoteUpdatingInfo;
import by.shnitko.error.NotFoundException;
import by.shnitko.repository.NoteRepository;
import by.shnitko.repository.entity.NoteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    @Transactional
    public void createNote(NoteCreatingInfo creatingInfo) {
        var entity = NoteEntity.toInstance(creatingInfo);
        noteRepository.save(entity);
    }

    @Transactional
    public void updateNote(NoteUpdatingInfo updatingInfo) {
        var saved = getEntity(updatingInfo.getId());
        var updated = NoteEntity.toInstance(saved, updatingInfo);
        noteRepository.save(updated);
    }

    public List<NoteEntity> getAll(UUID userId) {
        return noteRepository.findAllByUserId(userId);
    }

    public NoteEntity getById(UUID id) {
        return getEntity(id);
    }

    public void delete(UUID id) {
        noteRepository.deleteById(id);
    }

    private NoteEntity getEntity(UUID id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Note with id "+id+" not found"));
    }
}
