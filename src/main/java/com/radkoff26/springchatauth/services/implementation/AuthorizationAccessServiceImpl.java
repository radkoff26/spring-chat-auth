package com.radkoff26.springchatauth.services.implementation;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.radkoff26.springchatauth.domain.dto.AuthToken;
import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;
import com.radkoff26.springchatauth.services.declaration.AuthorizationAccessService;

@Service
public class AuthorizationAccessServiceImpl implements AuthorizationAccessService {
    private final RedisRepository<Long, AuthToken> userIdAccessTokenRepository;

    public AuthorizationAccessServiceImpl(RedisRepository<Long, AuthToken> userIdAccessTokenRepository) {
        this.userIdAccessTokenRepository = userIdAccessTokenRepository;
    }

    @Override
    public boolean hasAccess(long userId, String accessToken) {
        AuthToken authToken = userIdAccessTokenRepository.get(userId);
        if (authToken != null && authToken.accessToken().equals(accessToken)) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            return timestamp.before(authToken.expiresIn());
        }
        return false;
    }
}
