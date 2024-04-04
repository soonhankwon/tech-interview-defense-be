package dev.techmentordefensebe.chat.service;

import dev.techmentordefensebe.chat.domain.Chat;
import dev.techmentordefensebe.chat.domain.ChatMessage;
import dev.techmentordefensebe.chat.dto.request.ChatMentorAnswerRequest;
import dev.techmentordefensebe.chat.dto.response.ChatMentorAnswerResponse;
import dev.techmentordefensebe.chat.repository.ChatMessageRepository;
import dev.techmentordefensebe.chat.repository.ChatRepository;
import dev.techmentordefensebe.common.enumtype.ErrorCode;
import dev.techmentordefensebe.common.exception.CustomException;
import dev.techmentordefensebe.openai.dto.ChatCompletionMessageDTO;
import dev.techmentordefensebe.openai.dto.request.ChatCompletionRequest;
import dev.techmentordefensebe.openai.dto.response.ChatCompletionResponse;
import dev.techmentordefensebe.openai.service.OpenAiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMentorService {

    @Value("${open-ai.model}")
    private String model;
    public static final String CHAT_COMPLETION_ROLE_USER = "user";
    public static final String CHAT_COMPLETION_ROLE_ASSISTANT = "assistant";

    private final OpenAiService openAiService;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatMentorAnswerResponse findMentorAnswer(Long chatId, ChatMentorAnswerRequest request) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_CHAT_ID));

        // GPT model, 멘토 모드여부에 따른 멘토 프롬프트를 기본 세팅한 ChatCompletionRequest 생성
        ChatCompletionRequest defaultCompletionRequest = ChatCompletionRequest.ofDefaultSetting(model, chat,
                chat.getIsDefenseMode());

        // 대화 흐름을 위해 과거의 멘토링 채팅 메세지 이력을 포함한 chatCompletionRequest 생성
        ChatCompletionRequest chatCompletionRequest = getChatCompletionRequestWithPastHistory(chat,
                defaultCompletionRequest);

        // 요청된 유저 메세지 내용을 chatCompletionRequest 에 추가
        String userMessage = request.content();
        addUserChatMessageInChatCompletionRequest(userMessage, chatCompletionRequest);

        // 유저 메세지 DB insert
        saveChatMessage(userMessage, chat, true);

        // openAI 외부 API 로 chatCompletion 전송 및 응답 리턴
        ChatCompletionResponse chatCompletionResponse = openAiService.getChatCompletion(chatCompletionRequest);

        // AI 응답 파싱
        String aiAnswer = chatCompletionResponse.choices().getFirst()
                .message()
                .content();

        // AI 응답 DB insert
        saveChatMessage(aiAnswer, chat, false);
        // API 스펙에 맞게 변환 및 리턴
        return ChatMentorAnswerResponse.from(chatCompletionResponse);
    }

    private ChatCompletionRequest getChatCompletionRequestWithPastHistory(Chat chat,
                                                                          ChatCompletionRequest defaultCompletionRequest) {
        List<ChatMessage> chatMessageHistory = chatMessageRepository.findAllByChat(chat);
        return addPastChatMessageHistory(defaultCompletionRequest,
                chatMessageHistory);
    }

    private ChatCompletionRequest addPastChatMessageHistory(ChatCompletionRequest chatCompletion,
                                                            List<ChatMessage> chatMessages) {
        List<ChatCompletionMessageDTO> messages = chatCompletion.messages();
        chatMessages.forEach(chatMessage -> {
            ChatCompletionMessageDTO userMessage;
            if (chatMessage.getIsUserMessage()) {
                userMessage = ChatCompletionMessageDTO.of(CHAT_COMPLETION_ROLE_USER, chatMessage.getContent());
            } else {
                userMessage = ChatCompletionMessageDTO.of(CHAT_COMPLETION_ROLE_ASSISTANT, chatMessage.getContent());
            }
            messages.add(userMessage);
        });
        return chatCompletion;
    }

    private void addUserChatMessageInChatCompletionRequest(String content,
                                                           ChatCompletionRequest chatCompletionRequest) {
        ChatCompletionMessageDTO userQuestion = ChatCompletionMessageDTO.of(CHAT_COMPLETION_ROLE_USER, content);
        chatCompletionRequest.messages().add(userQuestion);
    }

    private void saveChatMessage(String content, Chat chat, boolean isUserMessage) {
        ChatMessage chatMessage = ChatMessage.of(content, chat, isUserMessage);
        chatMessageRepository.save(chatMessage);
    }
}
