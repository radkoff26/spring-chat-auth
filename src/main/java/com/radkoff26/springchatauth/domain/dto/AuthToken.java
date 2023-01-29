package com.radkoff26.springchatauth.domain.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public record AuthToken(String accessToken, String refreshToken, Timestamp expiresIn) implements Serializable {
}
