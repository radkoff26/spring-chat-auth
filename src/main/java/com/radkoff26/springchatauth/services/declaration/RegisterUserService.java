package com.radkoff26.springchatauth.services.declaration;

import org.springframework.http.HttpStatusCode;

import com.radkoff26.springchatauth.domain.entity.User;

public interface RegisterUserService {
    HttpStatusCode registerUser(User user);
}
