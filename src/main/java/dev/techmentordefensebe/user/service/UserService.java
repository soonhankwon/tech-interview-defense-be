package dev.techmentordefensebe.user.service;

import dev.techmentordefensebe.oauth.enumtype.OauthProvider;
import dev.techmentordefensebe.user.domain.User;
import dev.techmentordefensebe.user.dto.request.UserAddRequest;
import dev.techmentordefensebe.user.dto.response.UserAddResponse;
import dev.techmentordefensebe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserAddResponse addUser(UserAddRequest request) {
        if (request.oauthLoginType() == OauthProvider.NONE) {
            //TODO email 인증 로직
        }
        User user = request.toEntity();
        userRepository.save(user);
        return UserAddResponse.from(user);
    }
}
