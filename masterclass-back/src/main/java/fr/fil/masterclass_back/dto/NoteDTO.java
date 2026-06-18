package fr.fil.masterclass_back.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NoteDTO {
    private String id;
    private String content;
    private LocalDateTime date;
}