package com.radkoff26.springchatauth.utils;

import java.sql.Timestamp;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.radkoff26.springchatauth.domain.dto.AuthToken;

@Component
public class TokenGenerator {
    private static final String SYMBOLS = "abcdefghijklmnopqrstuvwxyz1234567890$#./";
    @Value("${security.token-length}")
    private int length;
    @Value("${security.lifespan_in_seconds}")
    private long lifespanInSeconds;

    public AuthToken generateToken() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + lifespanInSeconds * 1000);
        return new AuthToken(
                generateStringToken(),
                generateStringToken(),
                timestamp
        );
    }

    private String generateStringToken() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, length);
            sb.append(SYMBOLS.charAt(randomIndex));
        }

        return sb.toString();
    }
}
