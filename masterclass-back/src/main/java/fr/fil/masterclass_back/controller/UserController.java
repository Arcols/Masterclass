package fr.fil.masterclass_back.controller;

import fr.fil.masterclass_back.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import fr.fil.masterclass_back.model.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> GetUserById(@PathVariable String id) {
        return userService.GetUserById(id)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build()); // 404 si introuvable
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> UpdateUserById(@PathVariable String id, @RequestBody User userDetails) {

        if (userDetails.getUseId() != null && !userDetails.getUseId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        return userService.UpdateUserById(id, userDetails)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }
}