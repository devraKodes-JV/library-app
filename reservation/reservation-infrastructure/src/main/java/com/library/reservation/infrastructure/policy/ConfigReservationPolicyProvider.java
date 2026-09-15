package com.library.reservation.infrastructure.policy;

import com.library.config.application.service.ConfigService;
import com.library.reservation.domain.port.out.ReservationPolicyProvider;

import java.math.BigDecimal;

public class ConfigReservationPolicyProvider implements ReservationPolicyProvider {

    private final ConfigService configService;

    public ConfigReservationPolicyProvider(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public int getDefaultLoanDays() {
        return configService.getInt("reservation", "default.loan.days", 7);
    }

    @Override
    public int getDefaultDepositPercentage() {
        return configService.getInt("reservation", "deposit.percentage", 50);
    }

    @Override
    public BigDecimal getDefaultLateFeePerDay() {
        return configService.getDecimal("reservation", "late.fee.per.day", new BigDecimal("1.00"));
    }

    @Override
    public int getDefaultMaxRenewals() {
        return configService.getInt("reservation", "max.renewals", 1);
    }

    @Override
    public int getPickupDeadlineHours() {
        return configService.getInt("reservation", "pickup.deadline.hours", 48);
    }
}
