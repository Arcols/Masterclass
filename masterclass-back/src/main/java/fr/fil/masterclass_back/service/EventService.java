package fr.fil.masterclass_back.service;

import fr.fil.masterclass_back.dto.EventSummaryDTO;
import fr.fil.masterclass_back.model.Event;
import fr.fil.masterclass_back.model.EventType;
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

    public List<EventSummaryDTO> getTodoList() {
        List<Event> events = eventRepository.findFutureByTypes(
                List.of(EventType.DEVOIR, EventType.EXAMEN), LocalDate.now()
        );

        return events.stream()
                .map(EventSummaryDTO::from)
                .toList();
    }

}
