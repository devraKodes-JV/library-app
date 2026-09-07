package com.library.accounting.domain.model.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Money {
    public static final int SCALE = 2;
    public static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private Money() {}

    public static BigDecimal of(BigDecimal value) {
        if (value == null) return BigDecimal.ZERO.setScale(SCALE);
        return value.setScale(SCALE, ROUNDING);
    }

    public static BigDecimal of(String value) {
        if (value == null || value.isBlank()) return BigDecimal.ZERO.setScale(SCALE);
        return new BigDecimal(value).setScale(SCALE, ROUNDING);
    }
}
