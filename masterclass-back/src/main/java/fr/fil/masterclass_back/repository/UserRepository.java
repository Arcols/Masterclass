package fr.fil.masterclass_back.repository;

import fr.fil.masterclass_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUseMail(String mail);

    Optional<User> findByUseMail(String mail);
}
