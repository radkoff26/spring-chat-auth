package com.radkoff26.springchatauth.controllers;

import java.util.logging.Logger;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radkoff26.springchatauth.domain.entity.User;
import com.radkoff26.springchatauth.services.declaration.register.RegisterUserService;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = "http://localhost:3000")
public class RegisterController {
    private static final Logger LOGGER = Logger.getLogger(RegisterController.class.getName());
    private final RegisterUserService registerUserService;

    public RegisterController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @GetMapping
    public ResponseEntity<Void> registerUser(@RequestBody User user) {
        HttpStatusCode statusCode = registerUserService.registerUser(user);
        return ResponseEntity.status(statusCode).build();
    }
}
