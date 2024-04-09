package dev.techmentordefensebe.user.repository;

import dev.techmentordefensebe.user.domain.User;
import dev.techmentordefensebe.user.domain.UserTech;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTechRepository extends JpaRepository<UserTech, Long> {
    Optional<UserTech> findByIdAndUser(Long id, User user);
}
