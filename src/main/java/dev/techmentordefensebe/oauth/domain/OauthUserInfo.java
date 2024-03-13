package dev.techmentordefensebe.oauth.domain;

public interface OauthUserInfo {

    String getProviderId();

    String getProvider();

    String getEmail();

    String getNickname();
}
