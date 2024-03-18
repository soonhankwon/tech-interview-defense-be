package dev.techmentordefensebe.tech.controller;

import dev.techmentordefensebe.tech.dto.response.TechsGetResponse;
import dev.techmentordefensebe.tech.service.TechService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/techs")
public class TechController {

    private final TechService techService;

    @GetMapping
    public ResponseEntity<TechsGetResponse> getTechs(@RequestParam int pageNumber) {
        TechsGetResponse res = techService.findTechs(pageNumber);
        return ResponseEntity.ok().body(res);
    }
}
