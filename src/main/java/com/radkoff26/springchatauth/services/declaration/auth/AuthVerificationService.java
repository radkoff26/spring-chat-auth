package com.radkoff26.springchatauth.services.declaration.auth;

import com.radkoff26.springchatauth.domain.dto.AuthToken;

public interface AuthVerificationService {
    AuthToken verify(long userId, String code);
}
