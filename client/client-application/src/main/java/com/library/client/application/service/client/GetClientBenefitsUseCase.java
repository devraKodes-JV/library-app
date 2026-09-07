package com.library.client.application.service.client;

import com.library.client.application.dto.response.client.ClientBenefitsDTO;
import com.library.client.domain.model.ClientType;
import com.library.kernel.loan.LoanPolicy;

public class GetClientBenefitsUseCase {

    private final LoanPolicy loanPolicy;

    public GetClientBenefitsUseCase(LoanPolicy loanPolicy) {
        this.loanPolicy = loanPolicy;
    }

    public ClientBenefitsDTO execute(ClientType clientType) {
        return ClientBenefitsDTO.of(clientType, loanPolicy);
    }
}
