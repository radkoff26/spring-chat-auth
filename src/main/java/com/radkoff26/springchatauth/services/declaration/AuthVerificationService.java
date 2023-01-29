package com.radkoff26.springchatauth.services.declaration;

import com.radkoff26.springchatauth.domain.dto.AuthToken;

public interface AuthVerificationService {
    AuthToken verify(long userId, String code);
}
