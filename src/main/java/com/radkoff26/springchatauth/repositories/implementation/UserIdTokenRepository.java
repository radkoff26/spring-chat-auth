package com.radkoff26.springchatauth.repositories.implementation;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.radkoff26.springchatauth.domain.dto.AuthToken;
import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;

@Repository
public class UserIdTokenRepository extends RedisRepositoryImpl<Long, AuthToken> {
    private static final Long KEY = 0L;

    public UserIdTokenRepository(RedisTemplate<Long, AuthToken> userIdAccessTokenRedisTemplate) {
        super(userIdAccessTokenRedisTemplate.opsForHash(), KEY);
    }
}
