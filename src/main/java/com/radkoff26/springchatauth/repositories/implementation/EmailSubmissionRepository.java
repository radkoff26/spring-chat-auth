package com.radkoff26.springchatauth.repositories.implementation;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmailSubmissionRepository extends RedisRepositoryImpl<Long, String> {
    private static final Long KEY = 1L;

    public EmailSubmissionRepository(RedisTemplate<Long, String> redisTemplate) {
        super(redisTemplate.opsForHash(), KEY);
    }
}
