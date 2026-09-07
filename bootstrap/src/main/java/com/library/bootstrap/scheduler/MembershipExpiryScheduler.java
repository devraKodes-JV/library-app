package com.library.bootstrap.scheduler;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.bootstrap.factory.ClientFactory;
import com.library.client.application.service.client.ExpireMembershipsUseCase;
import com.library.client.domain.port.out.ClientRepository;

public class MembershipExpiryScheduler {

    private static final Logger log = LoggerFactory.getLogger(MembershipExpiryScheduler.class);

    private MembershipExpiryScheduler() {}

    public static int run(SessionFactory sessionFactory) {
        try {
            ClientRepository clientRepository = ClientFactory.clientRepository(sessionFactory);
            ExpireMembershipsUseCase useCase = new ExpireMembershipsUseCase(clientRepository);
            int count = useCase.execute();
            if (count > 0) {
                log.info("Membership expiry: demoted {} member(s) to CASUAL", count);
            }
            return count;
        } catch (Exception e) {
            log.error("Membership expiry scheduler failed: {}", e.getMessage(), e);
            return 0;
        }
    }
}
