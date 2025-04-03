package com.sourcery.km.service;

import java.util.Optional;

import com.sourcery.km.exception.EntityNotFound;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.sourcery.km.builder.user.UserBuilder;
import com.sourcery.km.dto.UserInfoDTO;
import com.sourcery.km.entity.User;
import com.sourcery.km.exception.UnauthorizedException;
import com.sourcery.km.exception.EntityExists;
import com.sourcery.km.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${okta.oauth2.issuer}")
    String auth0Domain;

    final static String userInfoPath = "/userinfo";

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;

    public UserInfoDTO getUserInfoFromAuth(Jwt token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserInfoDTO> response = restTemplate.exchange(
                auth0Domain + userInfoPath, HttpMethod.GET, entity, UserInfoDTO.class);
        return response.getBody();
    }

    public UserInfoDTO getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt token)) {
            throw new UnauthorizedException("User not authenticated");
        }
        String sub = token.getClaim("sub").toString();
        User user = userRepository.getUserWithAuth0ID(sub)
                .orElseThrow(() -> new EntityNotFound("User not found"));
        return UserBuilder.toUserInfoDTO(user);
    }

    @Transactional
    public void insertUser(Jwt token) {
        String auth0ID = token.getClaim("sub").toString();
        Optional<User> users = userRepository.getUserWithAuth0ID(auth0ID);
        if (users.isEmpty()) {
            UserInfoDTO userInfoDTO = getUserInfoFromAuth(token);
            User newUser = UserBuilder.toUserEntity(userInfoDTO);
            userRepository.insertUser(newUser);
        } else
            throw new EntityExists("User already exists");
    }
}
