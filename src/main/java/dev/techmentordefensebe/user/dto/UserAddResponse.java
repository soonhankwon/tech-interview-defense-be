package dev.techmentordefensebe.user.dto;

import dev.techmentordefensebe.oauth.enumtype.OauthProvider;
import dev.techmentordefensebe.user.domain.User;

public record UserAddResponse(
        Long id,
        String email,
        String nickname,
        String oauthProviderUniqueKey,
        OauthProvider oauthLoginType,
        String profileImgUrl
) {
    public static UserAddResponse from(User user) {
        return new UserAddResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getOauthProviderUniqueKey(),
                user.getOauthLoginType(),
                user.getProfileImgUrl()
        );
    }
}
