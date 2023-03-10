package com.radkoff26.springchatauth.domain.dto;

public enum AuthorizationResult {
    USER_NOT_FOUND,
    WRONG_PASSWORD,
    SUCCESSFULLY_AUTHORIZED,
    NEEDS_SECOND_FACTOR_SUBMISSION,
    AUTHORIZATION_INTERNAL_ERROR
}
