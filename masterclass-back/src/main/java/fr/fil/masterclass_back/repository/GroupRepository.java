package fr.fil.masterclass_back.repository;

import fr.fil.masterclass_back.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, String>
{
}
