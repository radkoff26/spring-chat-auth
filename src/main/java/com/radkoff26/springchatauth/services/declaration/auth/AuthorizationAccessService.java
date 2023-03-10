package com.radkoff26.springchatauth.services.declaration.auth;

public interface AuthorizationAccessService {
    boolean hasAccess(long userId, String accessToken);
}
