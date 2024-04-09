package dev.techmentordefensebe.user.controller;

import dev.techmentordefensebe.common.security.impl.UserDetailsImpl;
import dev.techmentordefensebe.user.dto.request.UserAddRequest;
import dev.techmentordefensebe.user.dto.request.UserTechAddRequest;
import dev.techmentordefensebe.user.dto.response.UserAddResponse;
import dev.techmentordefensebe.user.dto.response.UserTechAddResponse;
import dev.techmentordefensebe.user.dto.response.UserTechDeleteResponse;
import dev.techmentordefensebe.user.dto.response.UserTechsGetResponse;
import dev.techmentordefensebe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserAddResponse> addUser(@RequestBody UserAddRequest request) {
        UserAddResponse res = userService.addUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/techs")
    public ResponseEntity<UserTechAddResponse> addUserTech(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestBody UserTechAddRequest request) {
        UserTechAddResponse res = userService.addUserTech(userDetails, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/techs")
    public ResponseEntity<UserTechsGetResponse> getUserTech(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserTechsGetResponse res = userService.getUserTechs(userDetails);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/techs/{userTechId}")
    public ResponseEntity<UserTechDeleteResponse> deleteUserTech(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long userTechId) {
        UserTechDeleteResponse res = userService.deleteUserTech(userDetails, userTechId);
        return ResponseEntity.ok(res);
    }
}
