package com.radkoff26.springchatauth.services.implementation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.radkoff26.springchatauth.domain.body.SendEmailCodeRequestBody;
import com.radkoff26.springchatauth.domain.dto.AuthToken;
import com.radkoff26.springchatauth.domain.dto.AuthorizationResult;
import com.radkoff26.springchatauth.domain.dto.UserCredentials;
import com.radkoff26.springchatauth.domain.entity.User;
import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;
import com.radkoff26.springchatauth.services.declaration.AuthorizeService;
import com.radkoff26.springchatauth.services.declaration.UserService;
import com.radkoff26.springchatauth.utils.CodeGenerator;
import com.radkoff26.springchatauth.utils.TokenGenerator;

@Service
@PropertySource("classpath:application.yml")
public class AuthorizeServiceImpl implements AuthorizeService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final CodeGenerator codeGenerator;
    private final TokenGenerator tokenGenerator;
    private final RestTemplate restTemplate;
    private final RedisRepository<String, String> mailCodeRedisRepository;
    private final RedisRepository<Long, AuthToken> userIdAuthTokenRepository;
    @Value("${security.code-length}")
    private int codeLength;
    @Value("${async-task-url}")
    private String sendUrl;

    public AuthorizeServiceImpl(PasswordEncoder passwordEncoder, UserService userService, CodeGenerator codeGenerator, TokenGenerator tokenGenerator, RestTemplate restTemplate, RedisRepository<String, String> mailCodeRedisRepository, RedisRepository<Long, AuthToken> userIdAuthTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.codeGenerator = codeGenerator;
        this.tokenGenerator = tokenGenerator;
        this.restTemplate = restTemplate;
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

            SendEmailCodeRequestBody body = new SendEmailCodeRequestBody("SEND_EMAIL_CODE", map);

            restTemplate.postForEntity(sendUrl, body, void.class);
            mailCodeRedisRepository.add(user.getEmail(), code);

            return AuthorizationResult.NEEDS_SECOND_FACTOR_SUBMISSION;
        } else {
            AuthToken token = tokenGenerator.generateToken();

            userIdAuthTokenRepository.add(user.getId(), token);

            return AuthorizationResult.SUCCESSFULLY_AUTHORIZED;
        }
    }
}
