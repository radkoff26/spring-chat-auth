package com.radkoff26.springchatauth.services.implementation.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.radkoff26.springchatauth.domain.body.request.UserSubmissionEmailBody;
import com.radkoff26.springchatauth.domain.entity.User;
import com.radkoff26.springchatauth.services.declaration.user.UserService;

import jakarta.annotation.Nullable;

@Service
public class UserServiceImpl implements UserService {
    private final RestTemplate restTemplate;
    @Value("${get-user-url}")
    private String userUrl;

    public UserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Nullable
    public User getUserByLogin(String login) {
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(userUrl + "?login=" + login, User.class);
        if (responseEntity.getStatusCode() != HttpStatusCode.valueOf(200)) {
            return null;
        }
        return responseEntity.getBody();
    }

    @Override
    public User getUserById(long id) {
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(userUrl + "/" + id, User.class);
        if (responseEntity.getStatusCode() != HttpStatusCode.valueOf(200)) {
            return null;
        }
        return responseEntity.getBody();
    }

    @Override
    public void submitUserEmail(long id) {
        restTemplate.postForObject(userUrl + "/submitEmail", new UserSubmissionEmailBody(id), void.class);
    }
}
