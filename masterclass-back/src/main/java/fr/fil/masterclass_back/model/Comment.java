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
@Table(name = "Comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @Column(name = "COM_id", length = 50)
    private String comId;

    @Column(name = "COM_content", columnDefinition = "TEXT")
    private String comContent;

    @Column(name = "COM_date")
    private LocalDateTime comDate;

    @ManyToOne
    @JoinColumn(name = "EVE_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "USE_id", nullable = false)
    private User user;
}