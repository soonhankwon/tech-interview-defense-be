package dev.techmentordefensebe.user.service;

import dev.techmentordefensebe.common.enumtype.ErrorCode;
import dev.techmentordefensebe.common.exception.CustomException;
import dev.techmentordefensebe.common.security.impl.UserDetailsImpl;
import dev.techmentordefensebe.oauth.enumtype.OauthProvider;
import dev.techmentordefensebe.tech.domain.Tech;
import dev.techmentordefensebe.tech.repository.TechRepository;
import dev.techmentordefensebe.user.domain.User;
import dev.techmentordefensebe.user.domain.UserTech;
import dev.techmentordefensebe.user.dto.UserTechDTO;
import dev.techmentordefensebe.user.dto.request.UserAddRequest;
import dev.techmentordefensebe.user.dto.request.UserTechAddRequest;
import dev.techmentordefensebe.user.dto.response.UserAddResponse;
import dev.techmentordefensebe.user.dto.response.UserTechAddResponse;
import dev.techmentordefensebe.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TechRepository techRepository;

    @Transactional
    public UserAddResponse addUser(UserAddRequest request) {
        if (request.oauthLoginType() == OauthProvider.NONE) {
            //TODO email 인증 로직
        }
        User user = request.toEntity();
        userRepository.save(user);
        return UserAddResponse.from(user);
    }

    @Transactional
    public UserTechAddResponse addUserTech(UserDetailsImpl userDetails, UserTechAddRequest request) {
        String email = userDetails.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_USER_EMAIL));

        Tech tech = techRepository.findByName(request.techName())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXISTS_TECH_NAME));

        UserTech userTech = request.toEntity(user, tech);
        List<UserTech> userTechs = user.getUserTechs();

        //DB 쿼리로 중복체크를 하는대신 메모리에 올려서 중복체크
        boolean isAnyMatchRequestTechName = userTechs.stream()
                .map(ut -> ut.getTech().getName())
                .anyMatch(name -> tech.getName().equals(name));

        if (isAnyMatchRequestTechName) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.EXISTS_DUPLICATED_TECH_NAME);
        }
        userTechs.add(userTech);
        List<UserTechDTO> userTechDTOS = UserTechDTO.from(userTechs);
        return UserTechAddResponse.from(userTechDTOS);
    }
}
