package fr.fil.masterclass_back.service;

import fr.fil.masterclass_back.repository.UserRepository;
import org.springframework.stereotype.Service;
import fr.fil.masterclass_back.model.User;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final EmailService emailService;

    private final PasswordService passwordService;

    public UserService(UserRepository userRepository, EmailService emailService, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordService = passwordService;
    }

    private final Map<String, User> pendingUsers = new HashMap<>();

    public void registerUser(User user) {
        if (userRepository.existsByUseMail(user.getUseMail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // Hash le mot de passe
        String hashedPassword = passwordService.hash(user.getUsePassword());
        user.setUsePassword(hashedPassword);

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
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
}
