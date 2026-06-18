package fr.fil.masterclass_back.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class EventDetailDTO {
    private String id;
    private String type;
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

    private List<CommentDTO> comments;
    private List<NoteDTO> personalNotes;
}