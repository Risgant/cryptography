package by.shnitko.controller;

import by.shnitko.controller.dto.NoteDto;
import by.shnitko.controller.dto.NotesDto;
import by.shnitko.controller.dto.SingleNoteDto;
import by.shnitko.error.NotFoundException;
import by.shnitko.service.NoteService;
import by.shnitko.util.Encryptor;
import by.shnitko.util.PublicKeyCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(path = "/notepad")
@RequiredArgsConstructor
public class NoteController extends AbstractController {
    private final NoteService noteService;

    @GetMapping(path = "/{userId}/all", produces = {APPLICATION_JSON_VALUE})
    public NotesDto getAllNotes(@PathVariable UUID userId) {
        log.info("getAllNotes {}", userId);
        String publicKey = PublicKeyCache.getKey(userId);
        if(publicKey == null) {
            throw new NotFoundException("No public key found");
        }
        var notes = noteService.getAll(userId).stream()
                .map(entity -> new NoteDto(entity.getId(), entity.getTitle(), entity.getText()))
                .collect(Collectors.toList());
        log.info("getAllNotes {}", notes);
        return Encryptor.encryptSerpent(notes, publicKey);


//        List<NoteDto> dtos = new ArrayList<>();
//        if(!entities.isEmpty()) {
//            var texts = entities.stream().map(NoteEntity::getText).collect(Collectors.toList());
//            var encrypted = Encryptor.encryptRSA(texts, publicKey);
//
//            for(int i = 0; i < entities.size(); ++i) {
//                dtos.add(new NoteDto(entities.get(i).getId(), encrypted.get(i)));
//            }
//        }
//        return dtos;
    }

    //get encrypted noteDto by id
    @GetMapping(path = "/{userId}/{noteId}", produces = {APPLICATION_JSON_VALUE})
    public SingleNoteDto getNote(@PathVariable UUID userId, @PathVariable UUID noteId) {
        log.info("getNote {} {}", userId, noteId);
        String publicKey = PublicKeyCache.getKey(userId);
        if(publicKey == null) {
            throw new NotFoundException("No public key found");
        }
        var entity = noteService.getById(noteId);
        return Encryptor.encryptSerpent(entity.getText(), publicKey);
    }

//    @PostMapping(produces = {APPLICATION_JSON_VALUE})
//    public String createNote(@RequestBody NoteCreatingInfo creatingInfo) {
//        log.info("createNote {}", creatingInfo);
//        noteService.createNote(creatingInfo);
//        return "OK";
//    }
//
//    @PutMapping(produces = {APPLICATION_JSON_VALUE})
//    public String updateNote(@RequestBody NoteUpdatingInfo updatingInfo) {
//        log.info("updateNote {}", updatingInfo);
//        noteService.updateNote(updatingInfo);
//        return "OK";
//    }
//
//    @DeleteMapping(path = "/{id}", produces = {APPLICATION_JSON_VALUE})
//    public String deleteNote(@PathVariable UUID id) {
//        log.info("deleteNote {}", id);
//        noteService.delete(id);
//        return "OK";
//    }
}
