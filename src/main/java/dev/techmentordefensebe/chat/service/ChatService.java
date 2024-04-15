package dev.techmentordefensebe.chat.service;

import dev.techmentordefensebe.chat.domain.Chat;
import dev.techmentordefensebe.chat.domain.ChatMentor;
import dev.techmentordefensebe.chat.dto.ChatDTO;
import dev.techmentordefensebe.chat.dto.ChatMessageDTO;
import dev.techmentordefensebe.chat.dto.request.ChatAddRequest;
import dev.techmentordefensebe.chat.dto.response.ChatAddResponse;
import dev.techmentordefensebe.chat.dto.response.ChatDeleteResponse;
import dev.techmentordefensebe.chat.dto.response.ChatDetailsGetResponse;
import dev.techmentordefensebe.chat.dto.response.ChatsGetResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatService {

    private static final int PAGE_SIZE = 10;

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final TechRepository techRepository;

    @Transactional
    public ChatAddResponse addChat(UserDetailsImpl userDetails, ChatAddRequest request) {
        Long userId = userDetails.getUserId();

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_USER_ID));

        Tech tech = techRepository.findByName(request.topicTech())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_TECH_NAME));

        ChatMentor chatMentor = ChatMentor.from(request);
        Chat chat = saveChat(user, tech, chatMentor);
        return ChatAddResponse.of(user, tech, chat);
    }

    private Chat saveChat(User user, Tech tech, ChatMentor chatMentor) {
        Chat chat = Chat.of(user, tech, chatMentor);
        chatRepository.save(chat);
        return chat;
    }

    public ChatsGetResponse findChatsByUser(UserDetailsImpl userDetails, int pageNumber) {
        Long userId = userDetails.getUserId();

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_USER_ID));

        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Chat> page = chatRepository.findAllByUser(user, pageRequest);
        List<ChatDTO> chatDTOS = convertPageChatDTOS(page);
        return ChatsGetResponse.of(page.getTotalPages(), chatDTOS);
    }

    private List<ChatDTO> convertPageChatDTOS(Page<Chat> page) {
        return page.stream()
                .map(ChatDTO::from)
                .collect(Collectors.toList());
    }

    public ChatDetailsGetResponse findChatDetails(UserDetailsImpl userDetails, Long chatId) {
        Long userId = userDetails.getUserId();
        Chat chat = findChatByChatIdAndUserId(chatId, userId);
        List<ChatMessageDTO> chatMessages = chat.getChatMessages()
                .stream()
                .map(ChatMessageDTO::from)
                .toList();

        return ChatDetailsGetResponse.of(chat, chatMessages);
    }

    @Transactional
    public ChatDeleteResponse deleteChat(UserDetailsImpl userDetails, Long chatId) {
        Long userId = userDetails.getUserId();

        Chat chat = chatRepository.findByIdAndUser_Id(chatId, userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_CHAT_ID_WITH_USER));

        chatRepository.delete(chat);
        return ChatDeleteResponse.from(true);
    }

    private Chat findChatByChatIdAndUserId(Long chatId, Long userId) {
        return chatRepository.findByIdAndUser_Id(chatId, userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_CHAT_ID_WITH_USER));
    }
}
