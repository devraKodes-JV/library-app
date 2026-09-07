package com.library.bootstrap.generation;

import java.security.SecureRandom;
import java.util.Base64;

import com.library.kernel.generation.CodeGenerationService;

public class ShortUuidCodeGenerationService implements CodeGenerationService {

    private static final SecureRandom random = new SecureRandom();
    private static final int SUFFIX_LENGTH = 8;

    @Override
    public String generate(String prefix) {
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        String suffix = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
                .substring(0, Math.min(SUFFIX_LENGTH, SUFFIX_LENGTH));
        return prefix + "-" + suffix;
    }
}
