package fr.fil.masterclass_back.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import fr.fil.masterclass_back.model.EventType;

@Data
public class EventDetailDTO {
    private String id;
    private EventType type;
    private String title;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private String location;
    private String submissionLink;
    private String subjectName;
    private String groupName;
    private UserDTO creator;
    private boolean completed;

    private List<CommentDTO> comments;
    private List<NoteDTO> personalNotes;
}