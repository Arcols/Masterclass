package fr.fil.masterclass_back.repository;

import fr.fil.masterclass_back.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {
}