package com.radkoff26.springchatauth.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class CodeGenerator {
    private static final String SYMBOLS = "abcdefghijklmnopqrstuvwxyz1234567890$#./";

    public String generateAlphabetLetterCode(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, 'Z' - 'A');
            sb.append((char) ('A' + randomIndex));
        }

        return sb.toString();
    }

    public String generateTokenCode(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, length);
            sb.append(SYMBOLS.charAt(randomIndex));
        }

        return sb.toString();
    }
}
