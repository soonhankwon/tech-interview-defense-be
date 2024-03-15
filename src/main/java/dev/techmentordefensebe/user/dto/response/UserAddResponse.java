package dev.techmentordefensebe.user.dto.response;

import dev.techmentordefensebe.oauth.enumtype.OauthProvider;
import dev.techmentordefensebe.user.domain.User;

public record UserAddResponse(
        Long id,
        String email,
        String nickname,
        OauthProvider oauthLoginType,
        String profileImgUrl
) {
    public static UserAddResponse from(User user) {
        return new UserAddResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getOauthLoginType(),
                user.getProfileImgUrl()
        );
    }
}
