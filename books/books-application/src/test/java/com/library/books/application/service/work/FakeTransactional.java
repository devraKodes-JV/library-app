package com.library.books.application.service.work;

import java.util.function.Supplier;

import com.library.kernel.transaction.Transactional;

public class FakeTransactional implements Transactional {

    @Override
    public <T> T execute(Supplier<T> work) {
        return work.get();
    }
}
