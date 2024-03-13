package dev.techmentordefensebe.oauth.dto;

import dev.techmentordefensebe.oauth.domain.OauthUserInfo;

public record OauthLoginResponse(
        String oauthProviderUniqueKey,
        String email,
        String nickname,
        boolean isRegistered
) {
    public static OauthLoginResponse of(OauthUserInfo oauthUserInfo, boolean isRegistered) {
        return new OauthLoginResponse(
                oauthUserInfo.getProviderId(),
                oauthUserInfo.getEmail(),
                oauthUserInfo.getNickname(),
                isRegistered);
    }
}
