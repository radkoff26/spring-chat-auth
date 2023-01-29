package com.radkoff26.springchatauth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radkoff26.springchatauth.domain.dto.AuthToken;
import com.radkoff26.springchatauth.domain.dto.AuthorizationResult;
import com.radkoff26.springchatauth.domain.dto.UserCredentials;
import com.radkoff26.springchatauth.domain.entity.User;
import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;
import com.radkoff26.springchatauth.repositories.declaration.UserRepository;
import com.radkoff26.springchatauth.services.declaration.AuthVerificationService;
import com.radkoff26.springchatauth.services.declaration.AuthorizationAccessService;
import com.radkoff26.springchatauth.services.declaration.AuthorizeService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final AuthorizeService authorizeService;
    private final RedisRepository<Long, AuthToken> redisRepository;
    private final UserRepository userRepository;
    private final AuthVerificationService authVerificationService;
    private final AuthorizationAccessService authorizationAccessService;

    public AuthController(AuthorizeService authorizeService, RedisRepository<Long, AuthToken> redisRepository, UserRepository userRepository, AuthVerificationService authVerificationService, AuthorizationAccessService authorizationAccessService) {
        this.authorizeService = authorizeService;
        this.redisRepository = redisRepository;
        this.userRepository = userRepository;
        this.authVerificationService = authVerificationService;
        this.authorizationAccessService = authorizationAccessService;
    }

    @GetMapping(path = "/authorize")
    public ResponseEntity<Object> authorize(@RequestBody UserCredentials userCredentials) {
        AuthorizationResult authorizationResult = authorizeService.authorize(userCredentials);
        switch (authorizationResult) {
            case USER_NOT_FOUND -> {
                return ResponseEntity.notFound().build();
            }
            case WRONG_PASSWORD -> {
                return ResponseEntity.status(403).build();
            }
            case SUCCESSFULLY_AUTHORIZED -> {
                User user = userRepository.findUserByLogin(userCredentials.getLogin());
                AuthToken authToken = redisRepository.get(user.getId());
                return ResponseEntity.ok(authToken);
            }
            case NEEDS_SECOND_FACTOR_SUBMISSION -> {
                User user = userRepository.findUserByLogin(userCredentials.getLogin());
                return ResponseEntity.accepted().body(user.getId());
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(path = "/verify")
    public ResponseEntity<AuthToken> verify(@RequestParam("user_id") long userId, @RequestParam("code") String code) {
        AuthToken authToken = authVerificationService.verify(userId, code);

        if (authToken == null) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(authToken);
    }

    @GetMapping(path = "/access")
    public ResponseEntity<String> access(@RequestParam("user_id") long userId, @RequestParam("access_token") String token) {
        boolean hasAccess = authorizationAccessService.hasAccess(userId, token);
        return hasAccess ? ResponseEntity.ok("OK!") : ResponseEntity.status(403).body("Forbidden!");
    }
}
