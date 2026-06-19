package fr.fil.masterclass_back.repository;

import fr.fil.masterclass_back.model.Event;
import fr.fil.masterclass_back.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    @Query("SELECT e FROM Event e WHERE e.eveType IN :types AND e.eveDate >= :date ORDER BY e.eveDate ASC")
    List<Event> findFutureByTypes(@Param("types") List<EventType> types, @Param("date") LocalDate date);

    @Query("SELECT e FROM Event e " +
            "WHERE e.eveDate BETWEEN :startDate AND :endDate " +
            "AND e.group IN (SELECT g FROM User u JOIN u.groups g WHERE u.useId = :userId) " +
            "ORDER BY e.eveDate ASC, e.eveStarthour ASC")
    List<Event> findEventsForUserWeek(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("userId") String userId);
}
