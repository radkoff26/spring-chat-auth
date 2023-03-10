package com.radkoff26.springchatauth.controllers;

import java.util.logging.Logger;

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
import com.radkoff26.springchatauth.services.declaration.auth.AuthVerificationService;
import com.radkoff26.springchatauth.services.declaration.auth.AuthorizationAccessService;
import com.radkoff26.springchatauth.services.declaration.auth.AuthorizeService;
import com.radkoff26.springchatauth.services.declaration.auth.RefreshTokenService;
import com.radkoff26.springchatauth.services.declaration.user.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    private final AuthorizeService authorizeService;
    private final RedisRepository<Long, AuthToken> redisRepository;
    private final UserService userService;
    private final AuthVerificationService authVerificationService;
    private final AuthorizationAccessService authorizationAccessService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthorizeService authorizeService, RedisRepository<Long, AuthToken> redisRepository, UserService userService, AuthVerificationService authVerificationService, AuthorizationAccessService authorizationAccessService, RefreshTokenService refreshTokenService) {
        this.authorizeService = authorizeService;
        this.redisRepository = redisRepository;
        this.userService = userService;
        this.authVerificationService = authVerificationService;
        this.authorizationAccessService = authorizationAccessService;
        this.refreshTokenService = refreshTokenService;
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
                User user = userService.getUserByLogin(userCredentials.getLogin());
                AuthToken authToken = redisRepository.get(user.getId());
                return ResponseEntity.ok(authToken);
            }
            case NEEDS_SECOND_FACTOR_SUBMISSION -> {
                User user = userService.getUserByLogin(userCredentials.getLogin());
                return ResponseEntity.accepted().body(user.getId());
            }
            case AUTHORIZATION_INTERNAL_ERROR -> {
                return ResponseEntity.internalServerError().build();
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
        return hasAccess ? ResponseEntity.ok("OK! - 200") : ResponseEntity.status(403).body("Forbidden - 403");
    }

    @GetMapping(path = "/refresh")
    public ResponseEntity<AuthToken> refreshToken(@RequestParam("user_id") long userId, @RequestParam("refresh_token") String token) {
        AuthToken authToken = refreshTokenService.refreshToken(userId, token);
        if (authToken == null) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(authToken);
    }
}
