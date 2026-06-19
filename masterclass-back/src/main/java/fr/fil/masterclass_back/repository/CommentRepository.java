package fr.fil.masterclass_back.repository;

import fr.fil.masterclass_back.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    // récupère tous les commentaires liés à l'ID d'un événement
    List<Comment> findByEvent_EveIdOrderByComDateAsc(String eventId);
}