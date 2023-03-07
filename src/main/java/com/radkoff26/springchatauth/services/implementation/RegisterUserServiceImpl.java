package com.radkoff26.springchatauth.services.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.radkoff26.springchatauth.domain.entity.User;
import com.radkoff26.springchatauth.services.declaration.RegisterUserService;

@Service
public class RegisterUserServiceImpl implements RegisterUserService {
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    @Value("${get-user-url}")
    private String userUrl;

    public RegisterUserServiceImpl(PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    @Override
    public HttpStatusCode registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(userUrl, user, Void.class);
        return responseEntity.getStatusCode();
    }
}
