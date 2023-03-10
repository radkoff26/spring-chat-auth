package com.radkoff26.springchatauth.repositories.implementation;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MailCodeRepository extends RedisRepositoryImpl<String, String> {
    private static final String KEY = "Mail Code";

    public MailCodeRepository(RedisTemplate<String, String> mailCodeRedisTemplate) {
        super(mailCodeRedisTemplate.opsForHash(), KEY);
    }
}
