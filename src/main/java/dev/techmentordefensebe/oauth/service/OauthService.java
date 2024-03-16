package dev.techmentordefensebe.oauth.service;

import dev.techmentordefensebe.oauth.dto.OauthLoginResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface OauthService {

    OauthLoginResponse login(String provider, String code, HttpServletResponse httpServletResponse);
}
