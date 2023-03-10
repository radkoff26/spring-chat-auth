package com.radkoff26.springchatauth.repositories.implementation;

import org.springframework.data.redis.core.HashOperations;

import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;

public class RedisRepositoryImpl<K, V> implements RedisRepository<K, V> {
    private final HashOperations<K, K, V> hashOperations;
    private final K hashKey;

    public RedisRepositoryImpl(HashOperations<K, K, V> hashOperations, K hashKey) {
        this.hashOperations = hashOperations;
        this.hashKey = hashKey;
    }

    @Override
    public void add(K k, V v) {
        hashOperations.put(hashKey, k, v);
    }

    @Override
    public void delete(K k) {
        hashOperations.delete(hashKey, k);
    }

    @Override
    public V get(K k) {
        return hashOperations.get(hashKey, k);
    }
}
