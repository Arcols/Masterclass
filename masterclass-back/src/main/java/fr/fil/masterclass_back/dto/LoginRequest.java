package fr.fil.masterclass_back.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String mail;
    private String password;
}
