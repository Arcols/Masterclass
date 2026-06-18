package fr.fil.masterclass_back.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @Column(name = "EVE_id", length = 50)
    private String eveId;

    @Enumerated(EnumType.STRING)
    @Column(name = "EVE_type", length = 50)
    private EventType eveType;

    @Column(name = "EVE_title", length = 50)
    private String eveTitle;

    @Column(name = "EVE_date")
    private LocalDate eveDate;

    @Column(name = "EVE_starthour")
    private LocalTime eveStarthour;

    @Column(name = "EVE_endhour")
    private LocalTime eveEndhour;

    @Column(name = "EVE_description", columnDefinition = "TEXT")
    private String eveDescription;

    @Column(name = "EVE_location", length = 50)
    private String eveLocation;

    @Column(name = "EVE_submissionLink", length = 50)
    private String eveSubmissionLink;

    @ManyToOne
    @JoinColumn(name = "SUB_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "USE_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "GRO_id", nullable = false)
    private Group group;
}
