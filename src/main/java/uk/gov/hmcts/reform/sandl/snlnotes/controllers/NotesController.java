package uk.gov.hmcts.reform.sandl.snlnotes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.hmcts.reform.sandl.snlnotes.models.Note;
import uk.gov.hmcts.reform.sandl.snlnotes.repositories.NotesRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    private NotesRepository notesRepository;

    @GetMapping(path = "")
    public ResponseEntity<List<Note>> getAllNotes() {
        return ok(notesRepository.findAll());
    }

    @PostMapping(path = "/entities")
    public ResponseEntity<List<Note>> getNotesForEntities(@RequestBody List<UUID> ids) {
        return ok(notesRepository.findByEntityIdIn(ids));
    }

    @PostMapping(path = "/entities-dictionary")
    public ResponseEntity<Map<UUID, List<Note>>> getMapOfNotesForEntities(@RequestBody List<UUID> entityIds) {
        List<Note> noteList = notesRepository.findByEntityIdIn(entityIds);

        Map<UUID, List<Note>> notesMap = new HashMap<>();

        noteList.stream().forEach(n -> notesMap.computeIfAbsent(n.getEntityId(), k -> new ArrayList<>()).add(n));

        return ok(notesMap);
    }

    @PutMapping(path = "")
    public ResponseEntity<List<Note>> upsertMultipleNotes(@RequestBody List<Note> notes) {
        return ok(notesRepository.save(notes));
    }
}
