package com.library.books.application.service;

import com.library.kernel.generation.CodeGenerationService;

public class FakeCodeGenerationService implements CodeGenerationService {

    private final String prefix;
    private int counter = 1;

    public FakeCodeGenerationService(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String generate(String requestedPrefix) {
        return prefix + "-" + counter++;
    }
}
