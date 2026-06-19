package fr.fil.masterclass_back.dto;

import fr.fil.masterclass_back.model.EventType;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateEventDTO {
    private EventType type;
    private String title;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private String location;
    private String submissionLink;
    private String subjectId;  // nullable
    private String groupId;    // obligatoire
}
