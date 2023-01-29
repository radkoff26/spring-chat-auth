package com.radkoff26.springchatauth.repositories.implementation;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.radkoff26.springchatauth.domain.dto.AuthToken;
import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;

@Repository
public class UserIdTokenRepository implements RedisRepository<Long, AuthToken> {
    private static final Long KEY = 0L;
    private final HashOperations<Long, Long, AuthToken> hashOperations;

    public UserIdTokenRepository(RedisTemplate<Long, AuthToken> userIdAccessTokenRedisTemplate) {
        this.hashOperations = userIdAccessTokenRedisTemplate.opsForHash();
    }

    @Override
    public void add(Long key, AuthToken value) {
        hashOperations.put(KEY, key, value);
    }

    @Override
    public void delete(Long key) {
        hashOperations.delete(KEY, key);
    }

    @Override
    public AuthToken get(Long key) {
        return hashOperations.get(KEY, key);
    }
}
