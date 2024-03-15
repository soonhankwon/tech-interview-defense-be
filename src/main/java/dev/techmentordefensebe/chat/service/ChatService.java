package dev.techmentordefensebe.chat.service;

import dev.techmentordefensebe.chat.domain.Chat;
import dev.techmentordefensebe.chat.domain.ChatMentor;
import dev.techmentordefensebe.chat.dto.ChatMessageDTO;
import dev.techmentordefensebe.chat.dto.request.ChatAddRequest;
import dev.techmentordefensebe.chat.dto.response.ChatAddResponse;
import dev.techmentordefensebe.chat.dto.response.ChatDeleteResponse;
import dev.techmentordefensebe.chat.dto.response.ChatDetailsGetResponse;
import dev.techmentordefensebe.chat.dto.response.ChatGetResponse;
import dev.techmentordefensebe.chat.repository.ChatRepository;
import dev.techmentordefensebe.common.enumtype.ErrorCode;
import dev.techmentordefensebe.common.exception.CustomException;
import dev.techmentordefensebe.common.security.impl.UserDetailsImpl;
import dev.techmentordefensebe.tech.domain.Tech;
import dev.techmentordefensebe.tech.repository.TechRepository;
import dev.techmentordefensebe.user.domain.User;
import dev.techmentordefensebe.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final TechRepository techRepository;

    @Transactional
    public ChatAddResponse addChat(UserDetailsImpl userDetails, ChatAddRequest request) {
        String email = userDetails.getEmail();
        assert email != null;

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_USER_EMAIL));

        Tech tech = techRepository.findByName(request.topicTech())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_TOPIC_NAME));

        ChatMentor chatMentor = ChatMentor.from(request);
        Chat chat = Chat.of(user, tech, chatMentor);
        chatRepository.save(chat);

        return ChatAddResponse.of(user, tech, chat);
    }

    public List<ChatGetResponse> findChatsByUser(UserDetailsImpl userDetails) {
        String email = userDetails.getEmail();
        assert email != null;

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_USER_EMAIL));

        //TODO Paging 필요
        List<Chat> chats = chatRepository.findAllByUser(user);
        return chats.stream()
                .map(ChatGetResponse::from)
                .collect(Collectors.toList());
    }

    public ChatDetailsGetResponse findChatDetails(Long chatId) {
        Chat chat = findChatByChatId(chatId);
        List<ChatMessageDTO> chatMessages = chat.getChatMessages()
                .stream()
                .map(ChatMessageDTO::from)
                .toList();

        return ChatDetailsGetResponse.of(chat, chatMessages);
    }

    @Transactional
    public ChatDeleteResponse deleteChat(UserDetailsImpl userDetails, Long chatId) {
        String email = userDetails.getEmail();
        assert email != null;

        Chat chat = chatRepository.findByIdAndUser_Email(chatId, email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_USER));

        chatRepository.delete(chat);
        return ChatDeleteResponse.from(true);
    }

    private Chat findChatByChatId(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_CHAT_ID));
    }
}
