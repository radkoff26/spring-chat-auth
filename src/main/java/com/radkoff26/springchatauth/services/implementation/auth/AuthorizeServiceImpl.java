package com.radkoff26.springchatauth.services.implementation.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.radkoff26.springchatauth.domain.body.response.AsyncTaskCode;
import com.radkoff26.springchatauth.domain.dto.AuthToken;
import com.radkoff26.springchatauth.domain.dto.AuthorizationResult;
import com.radkoff26.springchatauth.domain.dto.UserCredentials;
import com.radkoff26.springchatauth.domain.entity.User;
import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;
import com.radkoff26.springchatauth.services.declaration.auth.AuthorizeService;
import com.radkoff26.springchatauth.services.declaration.send.SendAsyncTaskService;
import com.radkoff26.springchatauth.services.declaration.user.UserService;
import com.radkoff26.springchatauth.utils.CodeGenerator;
import com.radkoff26.springchatauth.utils.TokenGenerator;

@Service
@PropertySource("classpath:application.yml")
public class AuthorizeServiceImpl implements AuthorizeService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final CodeGenerator codeGenerator;
    private final TokenGenerator tokenGenerator;
    private final SendAsyncTaskService sendAsyncTaskService;
    private final RedisRepository<String, String> mailCodeRedisRepository;
    private final RedisRepository<Long, AuthToken> userIdAuthTokenRepository;
    @Value("${security.code-length}")
    private int codeLength;

    public AuthorizeServiceImpl(PasswordEncoder passwordEncoder, UserService userService, CodeGenerator codeGenerator, TokenGenerator tokenGenerator, SendAsyncTaskService sendAsyncTaskService, RedisRepository<String, String> mailCodeRedisRepository, RedisRepository<Long, AuthToken> userIdAuthTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.codeGenerator = codeGenerator;
        this.tokenGenerator = tokenGenerator;
        this.sendAsyncTaskService = sendAsyncTaskService;
        this.mailCodeRedisRepository = mailCodeRedisRepository;
        this.userIdAuthTokenRepository = userIdAuthTokenRepository;
    }

    @Override
    public AuthorizationResult authorize(UserCredentials userCredentials) {
        User user = userService.getUserByLogin(userCredentials.getLogin());

        if (user == null) {
            return AuthorizationResult.USER_NOT_FOUND;
        }

        boolean isUserVerified = passwordEncoder.matches(userCredentials.getPassword(), user.getPassword());

        if (!isUserVerified) {
            return AuthorizationResult.WRONG_PASSWORD;
        }

        if (user.isEmailVerified()) {
            String code = codeGenerator.generateAlphabetLetterCode(codeLength);

            Map<String, String> map = new HashMap<>(2);

            map.put("email", user.getEmail());
            map.put("code", code);

            ResponseEntity<Void> response = sendAsyncTaskService.sendAsyncTask(AsyncTaskCode.SEND_EMAIL_CODE, map);
            if (!response.getStatusCode().is2xxSuccessful()) {
                return AuthorizationResult.AUTHORIZATION_INTERNAL_ERROR;
            }
            mailCodeRedisRepository.add(user.getEmail(), code);

            return AuthorizationResult.NEEDS_SECOND_FACTOR_SUBMISSION;
        } else {
            AuthToken token = tokenGenerator.generateToken();

            userIdAuthTokenRepository.add(user.getId(), token);

            return AuthorizationResult.SUCCESSFULLY_AUTHORIZED;
        }
    }
}
