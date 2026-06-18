package fr.fil.masterclass_back.service;

import fr.fil.masterclass_back.model.Group;
import fr.fil.masterclass_back.repository.GroupRepository;
import fr.fil.masterclass_back.repository.UserRepository;
import org.springframework.stereotype.Service;
import fr.fil.masterclass_back.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private record ResetTokenData(String mail, long expiryEpochMillis) {}

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final EmailService emailService;

    private final PasswordService passwordService;

    public UserService(UserRepository userRepository, GroupRepository groupRepository, EmailService emailService, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordService = passwordService;
        this.groupRepository = groupRepository;
    }

    private final Map<String, User> pendingUsers = new HashMap<>();

    private final Map<String, ResetTokenData> resetTokens = new HashMap<>();

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

    public User connection(String mail, String password) {
        User user = userRepository.findByUseMail(mail)
                .orElseThrow(() -> new RuntimeException("Compte inexistant"));

        if (!passwordService.verify(password, user.getUsePassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        return user;
    }

    public void forgotPassword(String mail) {
        userRepository.findByUseMail(mail).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            long expiry = System.currentTimeMillis() + 30 * 60 * 1000; // 30 minutes
            resetTokens.put(token, new ResetTokenData(user.getUseMail(), expiry));
            emailService.sendResetPasswordEmail(user.getUseMail(), token);
        });
    }

    public void resetPassword(String token, String newPassword) {
        ResetTokenData data = resetTokens.get(token);
        if (data == null) {
            throw new RuntimeException("Lien invalide ou expiré");
        }
        if (System.currentTimeMillis() > data.expiryEpochMillis()) {
            resetTokens.remove(token);
            throw new RuntimeException("Lien expiré, veuillez refaire une demande");
        }

        User user = userRepository.findByUseMail(data.mail())
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        user.setUsePassword(passwordService.hash(newPassword));
        userRepository.save(user);

        resetTokens.remove(token);
    }

    public Optional<User> GetUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> UpdateUserById(String id, User userDetails) {
        return userRepository.findById(id).map(existingUser -> {
            if (userDetails.getUseFirstname() != null) {
                existingUser.setUseFirstname(userDetails.getUseFirstname());
            }
            if (userDetails.getUseLastname() != null) {
                existingUser.setUseLastname(userDetails.getUseLastname());
            }
            if (userDetails.getUseMail() != null) {
                existingUser.setUseMail(userDetails.getUseMail());
            }
            if (userDetails.getUseDescription() != null) {
                existingUser.setUseDescription(userDetails.getUseDescription());
            }
            if (userDetails.getGroups() != null) {
                List<String> groupIds = userDetails.getGroups().stream()
                        .map(Group::getGroId)
                        .collect(Collectors.toList());

                List<Group> realGroups = groupRepository.findAllById(groupIds);
                existingUser.setGroups(realGroups);
            }

            return userRepository.save(existingUser);
        });
    }
}
