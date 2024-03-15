package dev.techmentordefensebe.chat.repository;

import dev.techmentordefensebe.chat.domain.Chat;
import dev.techmentordefensebe.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByUser(User user);

    Optional<Chat> findByIdAndUser_Email(Long chatId, String email);
}
