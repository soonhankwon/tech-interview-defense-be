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
    private static final String DEFENSE_MODE = "defense";

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
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_TECH_NAME));

        ChatMentor chatMentor = ChatMentor.from(request);
        Chat chat = Chat.of(user, tech, chatMentor, request.isDefenseMode());
        chatRepository.save(chat);

        return ChatAddResponse.of(user, tech, chat);
    }

    public ChatsGetResponse findChatsByUser(UserDetailsImpl userDetails, int pageNumber, String mode) {
        String email = userDetails.getEmail();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_USER_EMAIL));

        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        //TODO mode validation 필요
        if (mode != null) {
            boolean isDefenseMode = isModeDefense(mode);
            Page<Chat> page = chatRepository.findAllByUserAndIsDefenseMode(user, isDefenseMode, pageRequest);
            List<ChatDTO> chatDTOS = convertPageChatDTOS(page);
            return ChatsGetResponse.of(page.getTotalPages(), chatDTOS);
        }
        Page<Chat> page = chatRepository.findAllByUser(user, pageRequest);
        List<ChatDTO> chatDTOS = convertPageChatDTOS(page);
        return ChatsGetResponse.of(page.getTotalPages(), chatDTOS);
    }

    private boolean isModeDefense(String mode) {
        return mode.equals(DEFENSE_MODE);
    }

    private List<ChatDTO> convertPageChatDTOS(Page<Chat> page) {
        return page.stream()
                .map(ChatDTO::from)
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
