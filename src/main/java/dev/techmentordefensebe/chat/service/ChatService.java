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
    private static final String MENTOR_MODE = "mentor";
    private static final String DEFENSE_MODE = "defense";

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

        Chat chat;
        ChatMentor chatMentor = ChatMentor.from(request);
        if (request.isDefenseMode()) {
            chat = saveChatByIsDefenseMode(user, tech, chatMentor, true);
            return ChatAddResponse.of(user, tech, chat);
        }
        chat = saveChatByIsDefenseMode(user, tech, chatMentor, false);
        return ChatAddResponse.of(user, tech, chat);
    }

    private Chat saveChatByIsDefenseMode(User user, Tech tech, ChatMentor chatMentor, boolean isDefenseMode) {
        // isDefenseMode 여부에 따라 Chat 을 생성하는 세부 로직이 달라짐
        Chat chat = Chat.of(user, tech, chatMentor, isDefenseMode);
        chatRepository.save(chat);
        return chat;
    }

    public ChatsGetResponse findChatsByUser(UserDetailsImpl userDetails, int pageNumber, String mode) {
        Long userId = userDetails.getUserId();

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_USER_ID));

        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        // mode 가 null 이 아닌경우 mode 는 mentor or defense 이다.(컨트롤러 레이어에서 validation 하기 때문에)
        if (mode != null) {
            assert mode.equals(MENTOR_MODE) || mode.equals(DEFENSE_MODE);
            boolean isDefenseMode = isModeDefense(mode);

            // mentor or defense 모드의 조회 결과를 리턴한다.
            Page<Chat> page = chatRepository.findAllByUserAndIsDefenseMode(user, isDefenseMode, pageRequest);
            List<ChatDTO> chatDTOS = convertPageChatDTOS(page);
            return ChatsGetResponse.of(page.getTotalPages(), chatDTOS);
        }
        // mode 가 null 인 경우 mentor, defense 모두를 포함한 조회 결과를 리턴한다.
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
