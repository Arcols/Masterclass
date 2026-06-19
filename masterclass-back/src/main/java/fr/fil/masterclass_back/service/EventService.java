package fr.fil.masterclass_back.service;

import fr.fil.masterclass_back.dto.*;
import fr.fil.masterclass_back.model.*;
import fr.fil.masterclass_back.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NoteRepository noteRepository;
    private final EventCompletionRepository eventCompletionRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;


    public EventService(EventRepository eventRepository, UserRepository userRepository, CommentRepository commentRepository, NoteRepository noteRepository, EventCompletionRepository eventCompletionRepository, GroupRepository groupRepository, SubjectRepository subjectRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.noteRepository = noteRepository;
        this.eventCompletionRepository = eventCompletionRepository;
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;

    }

    public EventDetailDTO getEventDetails(String eventId, String currentUserId) {
        // récupérer l'événement
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable avec l'ID : " + eventId));

        // mapper les infos de base
        EventDetailDTO dto = new EventDetailDTO();
        dto.setId(event.getEveId());
        dto.setType(event.getEveType());
        dto.setTitle(event.getEveTitle());
        dto.setDate(event.getEveDate());
        dto.setStartTime(event.getEveStarthour());
        dto.setEndTime(event.getEveEndhour());
        dto.setDescription(event.getEveDescription());
        dto.setLocation(event.getEveLocation());
        dto.setSubmissionLink(event.getEveSubmissionLink());

        boolean completed = eventCompletionRepository.existsByUserIdAndEventId(currentUserId, eventId);
        dto.setCompleted(completed);

        // Mapping du créateur avec le UserDTO
        UserDTO creatorDto = new UserDTO();
        creatorDto.setId(event.getUser().getUseId());
        creatorDto.setName(event.getUser().getUseFirstname() + " " + event.getUser().getUseLastname());
        dto.setCreator(creatorDto);

        // Mapping de la matière
        if (event.getSubject() != null) {
            dto.setSubjectName(event.getSubject().getSubName());
        }

        // Mapping du groupe
        if (event.getGroup() != null) {
            dto.setGroupName(event.getGroup().getGroName());
        }

        // récupérer et mapper les commentaires (Publics)
        List<Comment> comments = commentRepository.findByEvent_EveIdOrderByComDateAsc(eventId);
        dto.setComments(comments.stream().map(c -> {
            CommentDTO cDto = new CommentDTO();
            cDto.setId(c.getComId());
            cDto.setContent(c.getComContent());
            cDto.setDate(c.getComDate());
            cDto.setAuthorName(c.getUser().getUseFirstname() + " " + c.getUser().getUseLastname());
            return cDto;
        }).collect(Collectors.toList()));

        // récupérer et mapper les notes (Privées -> currentUserId)
        List<Note> notes = noteRepository.findByEvent_EveIdAndUser_UseIdOrderByNotDateAsc(eventId, currentUserId);
        dto.setPersonalNotes(notes.stream().map(n -> {
            NoteDTO nDto = new NoteDTO();
            nDto.setId(n.getNotId());
            nDto.setContent(n.getNotContent());
            nDto.setDate(n.getNotDate());
            return nDto;
        }).collect(Collectors.toList()));

        return dto;
    }

    public CommentDTO addComment(String eventId, String userId, String content) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Comment comment = new Comment();
        // On génère un ID unique pour le commentaire
        comment.setComId(java.util.UUID.randomUUID().toString());
        comment.setComContent(content);
        comment.setComDate(java.time.LocalDateTime.now());
        comment.setEvent(event);
        comment.setUser(user);

        commentRepository.save(comment);

        // On retourne le DTO pour que le front l'affiche directement
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getComId());
        dto.setContent(comment.getComContent());
        dto.setDate(comment.getComDate());
        dto.setAuthorName(user.getUseFirstname() + " " + user.getUseLastname());
        return dto;
    }

    public NoteDTO addNote(String eventId, String userId, String content) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Note note = new Note();
        note.setNotId(java.util.UUID.randomUUID().toString());
        note.setNotContent(content);
        note.setNotDate(java.time.LocalDateTime.now());
        note.setEvent(event);
        note.setUser(user);

        noteRepository.save(note);

        NoteDTO dto = new NoteDTO();
        dto.setId(note.getNotId());
        dto.setContent(note.getNotContent());
        dto.setDate(note.getNotDate());
        return dto;
    }

    public List<EventSummaryDTO> getTodoList(String userId) {
        List<Event> events = eventRepository.findFutureByTypes(
                List.of(EventType.DEVOIR, EventType.EXAMEN), LocalDate.now()
        );

        return events.stream()
                .map(e -> {
                    EventSummaryDTO dto = EventSummaryDTO.from(e);
                    // On vérifie en base si l'utilisateur a complété l'événement
                    boolean isCompleted = eventCompletionRepository.existsByUserIdAndEventId(userId, e.getEveId());
                    dto.setCompleted(isCompleted);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<EventSummaryDTO> getEventsForWeek(LocalDate startDate, LocalDate endDate, String userId) {
        // Récupération des événements depuis la BDD
        List<Event> events = eventRepository.findEventsForUserWeek(startDate, endDate, userId);

        // Conversion en DTO + Ajout du statut "completed"
        return events.stream().map(e -> {
            EventSummaryDTO dto = EventSummaryDTO.from(e);

            // On vérifie dans la BDD si cet utilisateur a fait ce devoir
            boolean isCompleted = eventCompletionRepository.existsByUserIdAndEventId(userId, e.getEveId());
            dto.setCompleted(isCompleted);

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public boolean toggleCompletion(String eventId, String userId) {
        if (eventCompletionRepository.existsByUserIdAndEventId(userId, eventId)) {
            // Si c'est déjà fait, on le décoche (on supprime la ligne)
            eventCompletionRepository.deleteByUserIdAndEventId(userId, eventId);
            return false;
        } else {
            // Sinon, on le marque comme fait (on crée la ligne)
            EventCompletion completion = new EventCompletion();
            completion.setId(java.util.UUID.randomUUID().toString());
            completion.setUserId(userId);
            completion.setEventId(eventId);
            eventCompletionRepository.save(completion);
            return true;
        }
    }

    public EventSummaryDTO createEvent(CreateEventDTO request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Groupe introuvable"));

        Subject subject = null;
        if (request.getSubjectId() != null && !request.getSubjectId().isBlank()) {
            subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Matière introuvable"));
        }

        Event event = new Event();
        event.setEveId(java.util.UUID.randomUUID().toString());
        event.setEveType(request.getType());
        event.setEveTitle(request.getTitle());
        event.setEveDate(request.getDate());
        event.setEveStarthour(request.getStartTime());
        event.setEveEndhour(request.getEndTime());
        event.setEveDescription(request.getDescription());
        event.setEveLocation(request.getLocation());
        event.setEveSubmissionLink(request.getSubmissionLink());
        event.setUser(user);
        event.setGroup(group);
        event.setSubject(subject);

        LocalTime startTime = request.getStartTime();
        LocalTime endTime = request.getEndTime();

        if (startTime == null && endTime != null) {
            startTime = endTime.minusHours(1);
        } else if (endTime == null && startTime != null) {
            endTime = startTime.plusHours(1);
        }

        event.setEveStarthour(startTime);
        event.setEveEndhour(endTime);

        eventRepository.save(event);

        return EventSummaryDTO.from(event);
    }
}
