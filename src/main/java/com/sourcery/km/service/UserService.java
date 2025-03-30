package com.sourcery.km.service;

import com.ctc.wstx.shaded.msv_core.driver.textui.Debug;
import com.sourcery.km.dto.UserInfoDTO;
import com.sourcery.km.entity.User;
import com.sourcery.km.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${okta.oauth2.issuer}")
    String auth0Domain;

    final String userInfoPath = "/userinfo";

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;

    @Transactional
    public UserInfoDTO getUserInfo(Jwt token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserInfoDTO> response = restTemplate.exchange(auth0Domain + userInfoPath, HttpMethod.GET, entity, UserInfoDTO.class);
        return response.getBody();
    }

    @Transactional
    public void insertUser(Jwt token) {
        String auth0ID = token.getClaim("sub").toString();
        List<User> users = userRepository.getUserWithAuth0ID(auth0ID);
        if (users.isEmpty()) {
            UserInfoDTO userInfoDTO = getUserInfo(token);
            User newUser = User.builder()
                    .email(userInfoDTO.getEmail())
                    .auth0_id(userInfoDTO.getSub())
                    .username(userInfoDTO.getName())
                    .build();
            userRepository.insertUser(newUser);
        } else
            throw new RuntimeException(String.format("User %s already exists", auth0ID));
    }
}
