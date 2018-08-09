package uk.gov.hmcts.reform.sandl.snlnotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.sandl.snlnotes.models.Note;

import java.util.List;
import java.util.UUID;

public interface NotesRepository extends JpaRepository<Note, UUID> {

    List<Note> findByEntityIdIn(List<UUID> uuids);
}
