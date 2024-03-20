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
    private static final String CHAT_COMPLETION_ROLE_USER = "user";
    private static final String CHAT_COMPLETION_ROLE_ASSISTANT = "assistant";

    private final OpenAiService openAiService;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatMentorAnswerResponse findMentorAnswer(Long chatId, ChatMentorAnswerRequest request) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_CHAT_ID));

        // Create Default Setting ChatCompletionRequest with gpt model, mentor prompt etc
        ChatCompletionRequest defaultCompletionRequest = ChatCompletionRequest.ofDefaultSetting(model, chat);

        // Add Past ChatMessage History in ChatCompletionRequest for Conversation flow
        ChatCompletionRequest chatCompletionRequest = getChatCompletionRequestWithPastHistory(chat,
                defaultCompletionRequest);

        // Add Request's user message content(question) in ChatCompletionRequest
        String userMessage = request.content();
        addUserChatMessageInChatCompletionRequest(userMessage, chatCompletionRequest);

        // Save userQuestion
        saveChatMessage(userMessage, chat, true);

        // Request open-ai API and return Response
        ChatCompletionResponse chatCompletionResponse = openAiService.getChatCompletion(chatCompletionRequest);

        // Get Response's AI message content
        String aiAnswer = chatCompletionResponse.choices().getFirst()
                .message()
                .content();

        // Save Response's AI Answer
        saveChatMessage(aiAnswer, chat, false);
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
