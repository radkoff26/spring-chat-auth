package com.radkoff26.springchatauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import com.radkoff26.springchatauth.domain.dto.AuthToken;

@Configuration
public class RedisConfig {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    // This template stores email-code data
    @Bean
    @Scope(value = "singleton")
    public RedisTemplate<String, String> mailCodeRedisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    // This template stores userId-token data
    @Bean
    @Scope(value = "singleton")
    public RedisTemplate<Long, AuthToken> userIdAccessTokenRedisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<Long, AuthToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(AuthToken.class));
        return redisTemplate;
    }

    // This template stores userId-hash data
    @Bean
    @Scope(value = "singleton")
    public RedisTemplate<Long, String> userEmailSubmissionRedisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<Long, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
}
