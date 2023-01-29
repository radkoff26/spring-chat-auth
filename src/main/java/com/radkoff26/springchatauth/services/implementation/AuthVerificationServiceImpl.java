package com.radkoff26.springchatauth.services.implementation;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.radkoff26.springchatauth.domain.dto.AuthToken;
import com.radkoff26.springchatauth.domain.entity.User;
import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;
import com.radkoff26.springchatauth.repositories.declaration.UserRepository;
import com.radkoff26.springchatauth.services.declaration.AuthVerificationService;
import com.radkoff26.springchatauth.utils.TokenGenerator;

@Service
public class AuthVerificationServiceImpl implements AuthVerificationService {
    private final RedisRepository<String, String> mailCodeRedisRepository;
    private final RedisRepository<Long, AuthToken> userIdAccessTokenRepository;
    private final TokenGenerator tokenGenerator;
    private final UserRepository userRepository;

    public AuthVerificationServiceImpl(RedisRepository<String, String> mailCodeRedisRepository, RedisRepository<Long, AuthToken> userIdAccessTokenRepository, TokenGenerator tokenGenerator, UserRepository userRepository) {
        this.mailCodeRedisRepository = mailCodeRedisRepository;
        this.userIdAccessTokenRepository = userIdAccessTokenRepository;
        this.tokenGenerator = tokenGenerator;
        this.userRepository = userRepository;
    }

    @Override
    public AuthToken verify(long userId, String code) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String codeInRepository = mailCodeRedisRepository.get(user.getEmail());
            if (codeInRepository != null && codeInRepository.equalsIgnoreCase(code)) {
                mailCodeRedisRepository.delete(user.getEmail());
                AuthToken authToken = tokenGenerator.generateToken();
                userIdAccessTokenRepository.add(userId, authToken);
                return authToken;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
