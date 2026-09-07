package com.library.kernel.loan;

/**
 * Generic loan policy contract. Implementations can be backed by hard-coded
 * defaults, persisted settings (configuration module), or a remote service.
 *
 * The interface is intentionally type-agnostic: callers pass the client type
 * as a string so this contract does not couple to any specific feature module.
 */
public interface LoanPolicy {

    int maxLoanDays(String clientType);

    int maxActiveReservations(String clientType);

    int renewalMaxDays(String clientType);

    int renewalMaxTimes(String clientType);

    boolean canRenew(String clientType);

    int maxRenewals(String clientType);
}
