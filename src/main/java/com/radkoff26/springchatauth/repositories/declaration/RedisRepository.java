package com.radkoff26.springchatauth.repositories.declaration;

public interface RedisRepository<K, V> {
    void add(K k, V v);
    void delete(K k);
    V get(K k);
}
