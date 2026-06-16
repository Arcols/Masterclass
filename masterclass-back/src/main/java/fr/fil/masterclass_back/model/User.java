package fr.fil.masterclass_back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "`User`")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "USE_id", length = 50)
    private String useId;

    @Column(name = "USE_firstname", length = 50)
    private String useFirstname;

    @Column(name = "USE_lastname", length = 50)
    private String useLastname;

    @Column(name = "USE_mail", length = 50)
    private String useMail;

    @Column(name = "USE_description", length = 50)
    private String useDescription;

    @Column(name = "USE_password", length = 50)
    private String usePassword;

    @ManyToMany
    @JoinTable(
            name = "User_Group",
            joinColumns = @JoinColumn(name = "USE_id"),
            inverseJoinColumns = @JoinColumn(name = "GRO_id")
    )
    private List<Group> groups;
}
