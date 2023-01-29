package com.radkoff26.springchatauth.services.declaration;

public interface AuthorizationAccessService {
    boolean hasAccess(long userId, String accessToken);
}
