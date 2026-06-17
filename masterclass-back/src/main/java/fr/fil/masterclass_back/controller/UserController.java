package fr.fil.masterclass_back.controller;

import fr.fil.masterclass_back.dto.RegisterRequest;
import fr.fil.masterclass_back.model.Group;
import fr.fil.masterclass_back.model.User;
import fr.fil.masterclass_back.repository.GroupRepository;
import fr.fil.masterclass_back.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final GroupRepository groupRepository;

    public UserController(UserService userService, GroupRepository groupRepository) {
        this.userService = userService;
        this.groupRepository = groupRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Les mots de passe ne correspondent pas");
            }

            if (request.getPassword().length() < 8) {
                return ResponseEntity.badRequest().body("Le mot de passe doit faire au moins 8 caractères");
            }
            if (!request.getPassword().matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
                return ResponseEntity.badRequest().body("Le mot de passe doit contenir au moins un caractère spécial");
            }

            // Mapping RegisterRequest → User
            User user = new User();
            user.setUseFirstname(request.getFirstname());
            user.setUseLastname(request.getLastname());
            user.setUseMail(request.getMail());
            user.setUseDescription(request.getDescription());
            user.setUsePassword(request.getPassword());

            // Mapping groupIds → List<Group>
            List<Group> groups = request.getGroupIds().stream()
                    .map(id -> groupRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Groupe introuvable : " + id)))
                    .collect(Collectors.toList());
            user.setGroups(groups);
            user.setGroups(groups);

            userService.registerUser(user);
            return ResponseEntity.ok("Email de confirmation envoyé");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        userService.confirmUser(token);
        return ResponseEntity.ok("Compte confirmé ! Vous pouvez vous connecter.");
    }

}
