package com.radkoff26.springchatauth.services.declaration.auth;

import com.radkoff26.springchatauth.domain.dto.AuthorizationResult;
import com.radkoff26.springchatauth.domain.dto.UserCredentials;

public interface AuthorizeService {
    AuthorizationResult authorize(UserCredentials userCredentials);
}
