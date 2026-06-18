package fr.fil.masterclass_back.controller;

import fr.fil.masterclass_back.model.Event;
import fr.fil.masterclass_back.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/todolist")
    public ResponseEntity<List<Event>> getTodoList() {

        return ResponseEntity.ok(eventService.getTodoList());
    }

}
