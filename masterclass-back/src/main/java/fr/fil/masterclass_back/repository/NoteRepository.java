package fr.fil.masterclass_back.repository;

import fr.fil.masterclass_back.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, String> {
    // récupère les notes d'un événement précis, POUR un utilisateur précis
    List<Note> findByEvent_EveIdAndUser_UseIdOrderByNotDateAsc(String eventId, String userId);
}