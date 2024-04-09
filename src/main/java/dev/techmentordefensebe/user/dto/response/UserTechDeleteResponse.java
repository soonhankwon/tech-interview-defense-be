package dev.techmentordefensebe.user.dto.response;

public record UserTechDeleteResponse(
        Long userTechId,
        Boolean isDeleted
) {
    public static UserTechDeleteResponse from(Long userTechId) {
        return new UserTechDeleteResponse(
                userTechId,
                true
        );
    }
}
