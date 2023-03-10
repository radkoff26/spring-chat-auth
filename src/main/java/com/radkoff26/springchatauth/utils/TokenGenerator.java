package com.radkoff26.springchatauth.utils;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.radkoff26.springchatauth.domain.dto.AuthToken;

@Component
public class TokenGenerator {
    private final CodeGenerator codeGenerator;
    @Value("${security.token-length}")
    private int length;
    @Value("${security.lifespan_in_seconds}")
    private long lifespanInSeconds;

    public TokenGenerator(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    public AuthToken generateToken() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + lifespanInSeconds * 1000);
        return new AuthToken(
                codeGenerator.generateTokenCode(length),
                codeGenerator.generateTokenCode(length),
                timestamp
        );
    }
}
