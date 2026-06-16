package fr.fil.masterclass_back.service;

import fr.fil.masterclass_back.repository.UserRepository;
import org.springframework.stereotype.Service;
import fr.fil.masterclass_back.model.User;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
