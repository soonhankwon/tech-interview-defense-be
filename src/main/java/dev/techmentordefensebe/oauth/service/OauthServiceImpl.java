package dev.techmentordefensebe.oauth.service;

import dev.techmentordefensebe.oauth.domain.OauthUserInfoImpl;
import dev.techmentordefensebe.oauth.dto.OauthLoginResponse;
import dev.techmentordefensebe.oauth.dto.OauthTokenDTO;
import dev.techmentordefensebe.oauth.enumtype.OauthProvider;
import dev.techmentordefensebe.user.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class OauthServiceImpl implements OauthService {

    private final InMemoryClientRegistrationRepository clientRegistrationRepository;
    private final UserRepository userRepository;

    @Override
    public OauthLoginResponse login(String provider, String code) {
        if (provider == null || provider.isEmpty()) {
            throw new IllegalArgumentException("provider can't null or empty");
        }
        OauthProvider.validateProvider(provider);
        assert provider.equals(OauthProvider.KAKAO.getValue()) || provider.equals(OauthProvider.GOOGLE.getValue());
        // application.yml에 등록된 해당 provider(kakao, google)의 Oauth 메타정보를 불러온다.
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(provider);
        OauthTokenDTO oauthToken = getOauthToken(code, clientRegistration);

        OauthUserInfoImpl oauthUserInfo = getUserInfoFromKakao(provider, oauthToken, clientRegistration);
        String oauthProviderUniqueKey = oauthUserInfo.getProviderId();

        //isRegistered: 서비스에 이미 가입된 유저인지 여부
        boolean isRegistered = userRepository.existsByOauthProviderUniqueKey(oauthProviderUniqueKey);
        return OauthLoginResponse.of(oauthUserInfo, isRegistered);
    }

    private OauthTokenDTO getOauthToken(String code, ClientRegistration clientRegistration) {
        return WebClient.create()
                .post()
                .uri(clientRegistration.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(createOauthRequestBody(code, clientRegistration))
                .retrieve()
                .bodyToMono(OauthTokenDTO.class)
                .block();
    }

    private MultiValueMap<String, String> createOauthRequestBody(String code, ClientRegistration clientRegistration) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", clientRegistration.getRedirectUri());
        formData.add("client_secret", clientRegistration.getClientSecret());
        formData.add("client_id", clientRegistration.getClientId());
        return formData;
    }

    private OauthUserInfoImpl getUserInfoFromKakao(String provider, OauthTokenDTO oauthToken,
                                                   ClientRegistration clientRegistration) {
        Map<String, Object> userAttributes = getUserAttribute(clientRegistration, oauthToken);
        return new OauthUserInfoImpl(userAttributes, provider);
    }

    private Map<String, Object> getUserAttribute(ClientRegistration clientRegistration, OauthTokenDTO oauthToken) {
        return WebClient.create()
                .get()
                .uri(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(String.valueOf(oauthToken.accessToken())))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }
}
