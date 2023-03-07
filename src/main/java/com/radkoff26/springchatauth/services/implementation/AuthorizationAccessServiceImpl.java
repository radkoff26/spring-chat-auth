package com.radkoff26.springchatauth.services.implementation;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.radkoff26.springchatauth.domain.dto.AuthToken;
import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;
import com.radkoff26.springchatauth.services.declaration.AuthorizationAccessService;

@Service
public class AuthorizationAccessServiceImpl implements AuthorizationAccessService {
    private final RedisRepository<Long, AuthToken> repository;
    @Value("${security.lifespan_in_seconds}")
    private int tokenLifeSpanInSeconds;

    public AuthorizationAccessServiceImpl(RedisRepository<Long, AuthToken> repository) {
        this.repository = repository;
    }

    @Override
    public boolean hasAccess(long userId, String accessToken) {
        AuthToken authToken = repository.get(userId);
        if (authToken != null && authToken.accessToken().equals(accessToken)) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            // If access token hasn't expired yet, then it's lifespan is extended
            if (timestamp.before(authToken.expiresIn())) {
                // TODO: create service to extend lifespan of the token
                long newExpirationTimeInMillis = timestamp.getTime() + (tokenLifeSpanInSeconds * 1000L);
                Timestamp newExpirationTimestamp = new Timestamp(newExpirationTimeInMillis);
                AuthToken updatedToken = new AuthToken(authToken.accessToken(), authToken.refreshToken(), newExpirationTimestamp);
                repository.add(userId, updatedToken);
                return true;
            }
        }
        return false;
    }
}
