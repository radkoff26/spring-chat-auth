package com.radkoff26.springchatauth.services.declaration;

import com.radkoff26.springchatauth.domain.dto.AuthToken;

public interface RefreshTokenService {
    AuthToken refreshToken(long id, String refreshToken);
}
