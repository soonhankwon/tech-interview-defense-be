package dev.techmentordefensebe.tech.repository;

import dev.techmentordefensebe.tech.domain.Tech;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechRepository extends JpaRepository<Tech, Long> {
    Optional<Tech> findByName(String name);
}
