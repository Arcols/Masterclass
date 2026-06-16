package fr.fil.masterclass_back.service;

import fr.fil.masterclass_back.repository.UserRepository;
import org.springframework.stereotype.Service;
import fr.fil.masterclass_back.model.User;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final EmailService emailService;

    public UserService(UserRepository userRepository,EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    private final Map<String, User> pendingUsers = new HashMap<>();

    public void registerUser(User user) {
        if (userRepository.existsByUseMail(user.getUseMail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // Génère un token de confirmation
        String token = UUID.randomUUID().toString();
        user.setUseId(UUID.randomUUID().toString());

        // Stocke l'user en attente de confirmation
        pendingUsers.put(token, user);

        emailService.sendConfirmationEmail(user.getUseMail(), token);
    }

    public void confirmUser(String token) {
        User user = pendingUsers.get(token);
        if (user == null) {
            throw new RuntimeException("Token invalide ou expiré");
        }
        userRepository.save(user);
        pendingUsers.remove(token);
    }
}
