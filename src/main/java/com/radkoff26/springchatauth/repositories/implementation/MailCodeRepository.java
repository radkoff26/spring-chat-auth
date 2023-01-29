package com.radkoff26.springchatauth.repositories.implementation;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;

@Repository
public class MailCodeRepository implements RedisRepository<String, String> {
    private static final String KEY = "Mail Code";
    private final HashOperations<String, String, String> hashOperations;

    public MailCodeRepository(RedisTemplate<String, String> mailCodeRedisTemplate) {
        this.hashOperations = mailCodeRedisTemplate.opsForHash();
    }

    @Override
    public void add(String key, String value) {
        hashOperations.put(KEY, key, value);
    }

    @Override
    public void delete(String key) {
        hashOperations.delete(KEY, key);
    }

    @Override
    public String get(String key) {
        return hashOperations.get(KEY, key);
    }
}
