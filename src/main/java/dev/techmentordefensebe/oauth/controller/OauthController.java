package dev.techmentordefensebe.oauth.controller;

import dev.techmentordefensebe.oauth.dto.OauthLoginResponse;
import dev.techmentordefensebe.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("{provider}")
    public ResponseEntity<OauthLoginResponse> oauthLogin(@PathVariable String provider,
                                                         @RequestParam String code) {

        OauthLoginResponse res = oauthService.login(provider, code);
        return ResponseEntity.ok().body(res);
    }
}
