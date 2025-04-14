package com.sourcery.km.controller;

import com.sourcery.km.dto.SessionDTO;
import com.sourcery.km.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;

    @GetMapping()
    public SessionDTO getSession() {
        return jwtService.createNewSession();
    }
}
