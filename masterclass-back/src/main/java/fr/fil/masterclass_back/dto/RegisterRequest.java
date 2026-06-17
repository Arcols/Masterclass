package fr.fil.masterclass_back.dto;

import lombok.Data;
import java.util.List;

@Data
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String description;
    private String mail;
    private String password;
    private String confirmPassword;
    private List<String> groupIds;
}
