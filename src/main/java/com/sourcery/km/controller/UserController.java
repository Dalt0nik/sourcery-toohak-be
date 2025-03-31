package com.sourcery.km.controller;

import com.sourcery.km.dto.UserInfoDTO;
import com.sourcery.km.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoDTO> getUser(@AuthenticationPrincipal Jwt principal) {
        try {
            UserInfoDTO userInfoDto = userService.getUserInfo(principal);
            return ResponseEntity.ok(userInfoDto);
        } catch (RuntimeException ignored) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/register")
    public ResponseEntity createUser(@AuthenticationPrincipal Jwt principal) {
        try {
            userService.insertUser(principal);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (RuntimeException ignored) {
            log.info(ignored.getMessage());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
