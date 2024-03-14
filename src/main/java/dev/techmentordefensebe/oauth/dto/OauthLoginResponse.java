package dev.techmentordefensebe.oauth.dto;

import dev.techmentordefensebe.oauth.domain.OauthUserInfo;

public record OauthLoginResponse(
        String oauthProviderUniqueKey,
        String email,
        String nickname,
        String profileImgUrl,
        boolean isRegistered
) {
    public static OauthLoginResponse from(OauthUserInfo oauthUserInfo, boolean isRegistered) {
        return new OauthLoginResponse(
                oauthUserInfo.getProviderId(),
                oauthUserInfo.getEmail(),
                oauthUserInfo.getNickname(),
                oauthUserInfo.getProfileImgUrl(),
                isRegistered
        );
    }
}
