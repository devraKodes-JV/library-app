package com.library.bootstrap.factory;

import org.hibernate.SessionFactory;

import com.library.accounting.application.service.payment.RecordClientPaymentUseCase;
import com.library.accounting.domain.port.out.PaymentRepository;
import com.library.accounting.infrastructure.persistence.adapter.PaymentPersistenceAdapter;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernatePaymentRepository;
import com.library.bootstrap.generation.ShortUuidCodeGenerationService;
import com.library.client.domain.port.out.ClientRepository;
import com.library.iam.infrastructure.notification.SseNotificationService;
import com.library.kernel.generation.CodeGenerationService;
import com.library.kernel.web.WebControllerContext;
import com.library.reservation.domain.port.out.PaymentRecorder;
import com.library.reservation.infrastructure.payment.AccountingPaymentRecorder;
import com.library.security.SecurityFactory;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.port.out.StockLocationRepository;
import com.library.stock.infrastructure.persistence.adapter.StockItemPersistenceAdapter;
import com.library.stock.infrastructure.persistence.adapter.StockLocationPersistenceAdapter;
import com.library.stock.infrastructure.persistence.repository.hibernate.HibernateStockItemRepository;
import com.library.stock.infrastructure.persistence.repository.hibernate.HibernateStockLocationRepository;

import io.javalin.config.JavalinConfig;

public final class AppFactory {

    private AppFactory() {}

    public static void create(SessionFactory sessionFactory, JavalinConfig config) {
        SecurityFactory.register(config, sessionFactory);

        SseNotificationService notificationService = new SseNotificationService();

        WebControllerContext webContext = IamFactory.register(config, sessionFactory);
        BooksFactory.register(config, sessionFactory, webContext);
        StockFactory.register(config, sessionFactory, webContext, notificationService);
        ClientFactory.register(config, sessionFactory, webContext);

        StockItemRepository stockItemRepository = new StockItemPersistenceAdapter(
                new HibernateStockItemRepository(sessionFactory));
        StockLocationRepository stockLocationRepository = new StockLocationPersistenceAdapter(
                new HibernateStockLocationRepository(sessionFactory));
        ClientRepository clientRepository = ClientFactory.clientRepository(sessionFactory);

        PaymentRepository paymentRepository = new PaymentPersistenceAdapter(
                new HibernatePaymentRepository(sessionFactory));
        CodeGenerationService codeGenerationService = new ShortUuidCodeGenerationService();
        RecordClientPaymentUseCase recordClientPaymentUseCase = new RecordClientPaymentUseCase(
                paymentRepository, codeGenerationService);
        PaymentRecorder paymentRecorder = new AccountingPaymentRecorder(recordClientPaymentUseCase);

        ReservationFactory.register(config, sessionFactory, webContext,
                stockItemRepository, stockLocationRepository, clientRepository,
                notificationService, paymentRecorder);
        AccountingFactory.register(config, sessionFactory, webContext, clientRepository);
    }
}
