package dev.techmentordefensebe.oauth.domain;

import static dev.techmentordefensebe.oauth.util.OauthUserInfoRequestConst.EMAIL;
import static dev.techmentordefensebe.oauth.util.OauthUserInfoRequestConst.GOOGLE_NICKNAME;
import static dev.techmentordefensebe.oauth.util.OauthUserInfoRequestConst.GOOGLE_PROFILE_IMG_URL;
import static dev.techmentordefensebe.oauth.util.OauthUserInfoRequestConst.ID;
import static dev.techmentordefensebe.oauth.util.OauthUserInfoRequestConst.KAKAO_ACCOUNT;
import static dev.techmentordefensebe.oauth.util.OauthUserInfoRequestConst.KAKAO_NICKNAME;
import static dev.techmentordefensebe.oauth.util.OauthUserInfoRequestConst.KAKAO_PROFILE_IMG_URL;
import static dev.techmentordefensebe.oauth.util.OauthUserInfoRequestConst.PROFILE;

import dev.techmentordefensebe.oauth.enumtype.OauthProvider;
import java.util.Map;
import lombok.RequiredArgsConstructor;

/*
인스턴스 생성시 일부 메서드에서
생성자의 provider 변수에 따라 카카오 또는 구글 계정 attribute 를 조회하는 로직이 분기됨
 */
@RequiredArgsConstructor
public class OauthUserInfoImpl implements OauthUserInfo {

    private final Map<String, Object> attributes;
    private final String provider;

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get(ID));
    }

    @Override
    public String getProvider() {
        String kakao = OauthProvider.KAKAO.getValue();
        if (this.provider.equals(kakao)) {
            return kakao;
        }
        return OauthProvider.GOOGLE.getValue();
    }

    @Override
    public String getEmail() {
        String kakao = OauthProvider.KAKAO.getValue();
        if (this.provider.equals(kakao)) {
            return (String) getKakaoAccount().get(EMAIL);
        }
        return (String) attributes.get(EMAIL);
    }

    @Override
    public String getNickname() {
        if (this.provider.equals(OauthProvider.KAKAO.getValue())) {
            return (String) getProfile().get(KAKAO_NICKNAME);
        }
        return (String) attributes.get(GOOGLE_NICKNAME);
    }

    @Override
    public String getProfileImgUrl() {
        if (this.provider.equals(OauthProvider.KAKAO.getValue())) {
            return (String) getProfile().get(KAKAO_PROFILE_IMG_URL);
        }
        return (String) attributes.get(GOOGLE_PROFILE_IMG_URL);
    }

    public Map<String, Object> getKakaoAccount() {
        return (Map<String, Object>) attributes.get(KAKAO_ACCOUNT);
    }

    public Map<String, Object> getProfile() {
        return (Map<String, Object>) getKakaoAccount().get(PROFILE);
    }
}
