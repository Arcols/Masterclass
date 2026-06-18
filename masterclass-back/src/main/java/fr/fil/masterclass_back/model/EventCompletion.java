package fr.fil.masterclass_back.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Event_Completion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCompletion {

    @Id
    private String id;

    @Column(name = "USE_id")
    private String userId;

    @Column(name = "EVE_id")
    private String eventId;
}