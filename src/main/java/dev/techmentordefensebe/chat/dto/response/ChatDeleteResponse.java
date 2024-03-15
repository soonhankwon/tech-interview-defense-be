package dev.techmentordefensebe.chat.dto.response;

public record ChatDeleteResponse(
        boolean isDeleted
) {
    public static ChatDeleteResponse from(boolean isDeleted) {
        return new ChatDeleteResponse(isDeleted);
    }
}
