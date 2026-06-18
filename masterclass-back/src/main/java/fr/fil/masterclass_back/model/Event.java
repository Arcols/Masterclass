package fr.fil.masterclass_back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @Column(name = "EVE_type", length = 50)
    private String eveType;

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
}