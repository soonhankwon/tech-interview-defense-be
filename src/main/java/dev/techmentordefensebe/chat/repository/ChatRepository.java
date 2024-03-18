package dev.techmentordefensebe.chat.repository;

import dev.techmentordefensebe.chat.domain.Chat;
import dev.techmentordefensebe.user.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Page<Chat> findAllByUser(User user, Pageable pageable);

    Page<Chat> findAllByUserAndIsDefenseMode(User user, Boolean isDefenseMode, Pageable pageable);

    Optional<Chat> findByIdAndUser_Email(Long chatId, String email);
}
