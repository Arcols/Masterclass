package fr.fil.masterclass_back.repository;

import fr.fil.masterclass_back.model.EventCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCompletionRepository extends JpaRepository<EventCompletion, String> {
    boolean existsByUserIdAndEventId(String userId, String eventId);
    void deleteByUserIdAndEventId(String userId, String eventId);
}