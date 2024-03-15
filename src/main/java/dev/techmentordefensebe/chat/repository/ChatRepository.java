package dev.techmentordefensebe.chat.repository;

import dev.techmentordefensebe.chat.domain.Chat;
import dev.techmentordefensebe.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByUser(User user);

    @Query("select distinct c from Chat c join fetch ChatMessage m on m.chat = c where :chatId = c.id")
    Chat findChatByChatId(Long chatId);
}
