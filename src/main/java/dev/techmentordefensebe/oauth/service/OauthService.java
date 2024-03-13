package dev.techmentordefensebe.oauth.service;

import dev.techmentordefensebe.oauth.dto.OauthLoginResponse;

public interface OauthService {
    OauthLoginResponse login(String provider, String code);
}
