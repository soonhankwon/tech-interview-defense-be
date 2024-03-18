package dev.techmentordefensebe.chat.dto.response;

import dev.techmentordefensebe.chat.dto.ChatDTO;
import java.util.List;

public record ChatsGetResponse(
        int totalPages,
        List<ChatDTO> chats

) {
    public static ChatsGetResponse of(int totalPages, List<ChatDTO> chats) {
        return new ChatsGetResponse(totalPages, chats);
    }
}
