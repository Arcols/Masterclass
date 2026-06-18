package fr.fil.masterclass_back.controller;

import java.util.Map;
import java.util.List;
import fr.fil.masterclass_back.dto.*;
import fr.fil.masterclass_back.model.*;
import fr.fil.masterclass_back.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final JwtService jwtService;

    public EventController(EventService eventService, JwtService jwtService) {
        this.eventService = eventService;
        this.jwtService = jwtService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventDetails(
            @PathVariable String id,
            HttpServletRequest request
    ) {
        try {
            // extraction du token depuis le header "Authorization"
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token manquant ou mal formaté");
            }

            String token = authHeader.substring(7);

            // validation du token
            if (!jwtService.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide ou expiré");
            }

            // récupération de l'ID utilisateur depuis le token
            String userId = jwtService.extractUserId(token);

            // appel du service avec le bon ID
            EventDetailDTO eventDetails = eventService.getEventDetails(id, userId);

            return ResponseEntity.ok(eventDetails);

        } catch (RuntimeException e) {
            // RuntimeException catché ici si l'on vient du "orElseThrow" dans le EventService
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur inattendue est survenue");
        }
    }

    @PostMapping("/{eventId}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable String eventId,
            @RequestBody Map<String, String> payload) {

        String userId = payload.get("userId");
        String content = payload.get("content");

        return ResponseEntity.ok(eventService.addComment(eventId, userId, content));
    }

    @PostMapping("/{eventId}/notes")
    public ResponseEntity<NoteDTO> addNote(
            @PathVariable String eventId,
            @RequestBody Map<String, String> payload) {

        String userId = payload.get("userId");
        String content = payload.get("content");

        return ResponseEntity.ok(eventService.addNote(eventId, userId, content));
    }

    @GetMapping("/todolist")
    public ResponseEntity<List<EventSummaryDTO>> getTodoList() {

        return ResponseEntity.ok(eventService.getTodoList());
    }

    @PostMapping("/{eventId}/toggle-completion")
    public ResponseEntity<Boolean> toggleCompletion(
            @PathVariable String eventId,
            @RequestBody Map<String, String> payload) {

        String userId = payload.get("userId");
        boolean isNowCompleted = eventService.toggleCompletion(eventId, userId);

        return ResponseEntity.ok(isNowCompleted);
    }
}