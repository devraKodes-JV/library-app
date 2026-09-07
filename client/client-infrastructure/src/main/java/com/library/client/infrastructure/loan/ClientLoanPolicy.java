package com.library.client.infrastructure.loan;

import com.library.client.domain.model.ClientType;
import com.library.kernel.loan.LoanPolicy;

public class ClientLoanPolicy implements LoanPolicy {

    private static final int CASUAL_MAX_LOAN_DAYS = 7;
    private static final int MEMBER_MAX_LOAN_DAYS = 21;

    private static final int CASUAL_MAX_ACTIVE_RESERVATIONS = 2;
    private static final int MEMBER_MAX_ACTIVE_RESERVATIONS = 5;

    private static final int CASUAL_RENEWAL_MAX_DAYS = 7;
    private static final int MEMBER_RENEWAL_MAX_DAYS = 14;

    private static final int CASUAL_RENEWAL_MAX_TIMES = 1;
    private static final int MEMBER_RENEWAL_MAX_TIMES = 3;

    private static final int CASUAL_MAX_RENEWALS = 1;
    private static final int MEMBER_MAX_RENEWALS = 3;

    @Override
    public int maxLoanDays(String clientType) {
        if (ClientType.MEMBER.name().equalsIgnoreCase(clientType)) {
            return MEMBER_MAX_LOAN_DAYS;
        }
        return CASUAL_MAX_LOAN_DAYS;
    }

    @Override
    public int maxActiveReservations(String clientType) {
        if (ClientType.MEMBER.name().equalsIgnoreCase(clientType)) {
            return MEMBER_MAX_ACTIVE_RESERVATIONS;
        }
        return CASUAL_MAX_ACTIVE_RESERVATIONS;
    }

    @Override
    public int renewalMaxDays(String clientType) {
        if (ClientType.MEMBER.name().equalsIgnoreCase(clientType)) {
            return MEMBER_RENEWAL_MAX_DAYS;
        }
        return CASUAL_RENEWAL_MAX_DAYS;
    }

    @Override
    public int renewalMaxTimes(String clientType) {
        if (ClientType.MEMBER.name().equalsIgnoreCase(clientType)) {
            return MEMBER_RENEWAL_MAX_TIMES;
        }
        return CASUAL_RENEWAL_MAX_TIMES;
    }

    @Override
    public boolean canRenew(String clientType) {
        return true;
    }

    @Override
    public int maxRenewals(String clientType) {
        if (ClientType.MEMBER.name().equalsIgnoreCase(clientType)) {
            return MEMBER_MAX_RENEWALS;
        }
        return CASUAL_MAX_RENEWALS;
    }
}
