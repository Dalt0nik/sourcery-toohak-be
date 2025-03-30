package com.sourcery.km.service;

import com.sourcery.km.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${okta.oauth2.issuer}")
    String auth0Domain;

    final String userInfoPath = "/userinfo";

    private final RestTemplate restTemplate;

    public UserInfoDTO getUserInfo(Jwt token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserInfoDTO> response = restTemplate.exchange(auth0Domain + userInfoPath, HttpMethod.GET, entity, UserInfoDTO.class);
        return response.getBody();
    }
}
