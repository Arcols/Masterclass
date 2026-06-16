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
import java.time.LocalDateTime;

@Entity
@Table(name = "Note")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @Column(name = "NOT_id", length = 50)
    private String notId;

    @Column(name = "NOT_content", columnDefinition = "TEXT")
    private String notContent;

    @Column(name = "NOT_date")
    private LocalDateTime notDate;

    @ManyToOne
    @JoinColumn(name = "EVE_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "USE_id", nullable = false)
    private User user;
}