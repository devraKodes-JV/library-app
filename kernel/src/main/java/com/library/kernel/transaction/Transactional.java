package com.library.kernel.transaction;

import java.util.function.Supplier;

public interface Transactional {

    <T> T execute(Supplier<T> work);
}
