package com.library.bootstrap.factory;

import org.hibernate.SessionFactory;

import com.library.bootstrap.generation.ShortUuidCodeGenerationService;
import com.library.bootstrap.web.LandingController;
import com.library.client.domain.port.out.ClientRepository;
import com.library.iam.infrastructure.notification.SseNotificationService;
import com.library.kernel.generation.CodeGenerationService;
import com.library.kernel.web.WebControllerContext;
import com.library.reservation.domain.port.out.PaymentRecorder;
import com.library.security.SecurityFactory;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.port.out.StockLocationRepository;

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
        ConfigFactory.register(config, sessionFactory, webContext);

        StockItemRepository stockItemRepository = StockFactory.stockItemRepository(sessionFactory);
        StockLocationRepository stockLocationRepository = StockFactory.stockLocationRepository(sessionFactory);
        ClientRepository clientRepository = ClientFactory.clientRepository(sessionFactory);

        CodeGenerationService codeGenerationService = new ShortUuidCodeGenerationService();
        PaymentRecorder paymentRecorder = AccountingFactory.paymentRecorder(sessionFactory, codeGenerationService);

        ReservationFactory.register(config, sessionFactory, webContext,
                stockItemRepository, stockLocationRepository, clientRepository,
                notificationService, paymentRecorder);
        AccountingFactory.register(config, sessionFactory, webContext, clientRepository);
        CatalogFactory.register(config, sessionFactory, stockItemRepository);

        LandingController landingController = new LandingController();
        config.routes.get("/landing", landingController::show);
    }
}
