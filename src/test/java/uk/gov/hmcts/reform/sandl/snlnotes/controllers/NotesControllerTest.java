package uk.gov.hmcts.reform.sandl.snlnotes.controllers;

import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import uk.gov.hmcts.reform.sandl.snlnotes.models.Note;
import uk.gov.hmcts.reform.sandl.snlnotes.repositories.NotesRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class NotesControllerTest {

    @InjectMocks
    private NotesController notesController;

    @Mock
    private NotesRepository notesRepository;

    private static final List<Note> notes = Arrays.asList(new Note());

    @Test
    public void getAllNotes_shouldReturnMockedNotes() {
        when(notesRepository.findAll()).thenReturn(notes);

        assertEquals(notes, notesController.getAllNotes().getBody());
    }

    @Test
    public void upsertMultipleNotes_shouldReturnSavedNotes() {
        when(notesRepository.save(notes)).thenReturn(notes);

        val result = notesController.upsertMultipleNotes(notes);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(notes, result.getBody());
    }
}
