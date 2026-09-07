package com.library.client.application.dto.response.client;

import com.library.client.domain.model.ClientType;
import com.library.kernel.loan.LoanPolicy;

public record ClientBenefitsDTO(
        String clientType,
        String clientTypeLabel,
        int maxLoanDays,
        int maxActiveReservations,
        int renewalMaxDays,
        int renewalMaxTimes,
        int maxRenewals) {

    public static ClientBenefitsDTO of(ClientType type, LoanPolicy loanPolicy) {
        String typeName = type.name();
        return new ClientBenefitsDTO(
                typeName,
                type.getLabel(),
                loanPolicy.maxLoanDays(typeName),
                loanPolicy.maxActiveReservations(typeName),
                loanPolicy.renewalMaxDays(typeName),
                loanPolicy.renewalMaxTimes(typeName),
                loanPolicy.maxRenewals(typeName));
    }
}
