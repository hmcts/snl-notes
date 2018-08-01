package uk.gov.hmcts.reform.sandl.snlnotes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlnotes.models.Note;
import uk.gov.hmcts.reform.sandl.snlnotes.repositories.NotesRepository;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    private NotesRepository notesRepository;


    @GetMapping(path = "")
    public ResponseEntity<List<Note>> getAllNotes() {
        return ok(notesRepository.findAll());
    }

    @PutMapping(path = "")
    public ResponseEntity<List<Note>> upsertMultipleNotes(@RequestBody List<Note> notes) {
        return ok(notesRepository.save(notes));
    }
}
