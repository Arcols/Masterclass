package fr.fil.masterclass_back.dto;

import fr.fil.masterclass_back.model.Event;
import fr.fil.masterclass_back.model.EventType;
import fr.fil.masterclass_back.model.Subject;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventSummaryDTO {
    private String eveId;
    private EventType eveType;
    private String eveTitle;
    private LocalDate eveDate;
    private LocalTime eveStarthour;
    private LocalTime eveEndhour;
    private String eveDescription;
    private String eveLocalisation;
    private String eveLien;
    private Subject subject;

    public static EventSummaryDTO from(Event e) {
        EventSummaryDTO dto = new EventSummaryDTO();
        dto.eveId = e.getEveId();
        dto.eveType = e.getEveType();
        dto.eveTitle = e.getEveTitle();
        dto.eveDate = e.getEveDate();
        dto.eveStarthour = e.getEveStarthour();
        dto.eveEndhour = e.getEveEndhour();
        dto.eveDescription = e.getEveDescription();
        dto.eveLocalisation = e.getEveLocalisation();
        dto.eveLien = e.getEveLien();
        dto.subject = e.getSubject();
        return dto;
    }
}
