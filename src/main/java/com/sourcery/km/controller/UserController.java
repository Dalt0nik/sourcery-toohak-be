package com.sourcery.km.controller;

import com.sourcery.km.dto.UserInfoDTO;
import com.sourcery.km.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserInfoDTO getUser(@AuthenticationPrincipal Jwt principal) {
        return userService.getUserInfo(principal);
    }
}
