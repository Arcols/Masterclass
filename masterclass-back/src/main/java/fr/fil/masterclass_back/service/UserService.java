package fr.fil.masterclass_back.service;

import fr.fil.masterclass_back.model.Group;
import fr.fil.masterclass_back.repository.GroupRepository;
import fr.fil.masterclass_back.repository.UserRepository;
import org.springframework.stereotype.Service;
import fr.fil.masterclass_back.model.User;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public UserService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public Optional<User> GetUserById(String id)
    {
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
