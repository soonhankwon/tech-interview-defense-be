package dev.techmentordefensebe.user.repository;

import dev.techmentordefensebe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByOauthProviderUniqueKey(String oauthProviderUniqueKey);
}
