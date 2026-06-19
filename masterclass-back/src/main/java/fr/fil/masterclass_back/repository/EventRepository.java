package fr.fil.masterclass_back.repository;

import fr.fil.masterclass_back.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findByUser_UseIdAndEveDateBetween(String userId, LocalDate startDate, LocalDate endDate);
}