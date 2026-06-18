package fr.fil.masterclass_back.controller;

import fr.fil.masterclass_back.model.Event;
import fr.fil.masterclass_back.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/planning")
    public ResponseEntity<List<Event>> getEventsByUserAndDates(
            @RequestParam String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // On appelle le nom simple défini dans le service
        List<Event> events = eventService.findEventsByUserAndDates(userId, startDate, endDate);
        return ResponseEntity.ok(events);
    }
}