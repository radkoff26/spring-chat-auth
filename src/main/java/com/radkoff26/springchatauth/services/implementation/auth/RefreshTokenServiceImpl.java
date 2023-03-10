package com.radkoff26.springchatauth.services.implementation.auth;

import org.springframework.stereotype.Service;

import com.radkoff26.springchatauth.domain.dto.AuthToken;
import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;
import com.radkoff26.springchatauth.services.declaration.auth.RefreshTokenService;
import com.radkoff26.springchatauth.utils.TokenGenerator;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RedisRepository<Long, AuthToken> repository;
    private final TokenGenerator tokenGenerator;

    public RefreshTokenServiceImpl(RedisRepository<Long, AuthToken> repository, TokenGenerator tokenGenerator) {
        this.repository = repository;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public AuthToken refreshToken(long id, String refreshToken) {
        AuthToken previousToken = repository.get(id);
        if (previousToken.refreshToken().equals(refreshToken)) {
            AuthToken newToken = tokenGenerator.generateToken();
            repository.add(id, newToken);
            return newToken;
        }
        return null;
    }
}
