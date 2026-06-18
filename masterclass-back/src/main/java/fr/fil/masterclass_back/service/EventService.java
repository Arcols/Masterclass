package fr.fil.masterclass_back.service;

import fr.fil.masterclass_back.model.Event;
import fr.fil.masterclass_back.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findEventsByUserAndDates(String userId, LocalDate startDate, LocalDate endDate) {
        return eventRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }
}