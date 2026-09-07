package com.library.bootstrap.factory;

import org.hibernate.SessionFactory;

import com.library.books.application.service.edition.GetEditionUseCase;
import com.library.books.application.service.edition.ListEditionsUseCase;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.domain.port.out.PublisherRepository;
import com.library.books.domain.port.out.WorkRepository;
import com.library.books.infrastructure.persistence.adapter.BookFormatPersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.EditionPersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.LanguagePersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.PublisherPersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.WorkPersistenceAdapter;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateBookFormatRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateEditionAuthorRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateEditionRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateLanguageRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernatePublisherRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateWorkRepository;
import com.library.client.application.service.client.CreateClientUseCase;
import com.library.client.application.service.client.GetClientUseCase;
import com.library.client.application.service.client.GetClientByDniUseCase;
import com.library.client.application.service.client.ListClientsUseCase;
import com.library.client.domain.port.out.ClientRepository;
import com.library.iam.domain.port.out.NotificationService;
import com.library.kernel.generation.CodeGenerationService;
import com.library.kernel.loan.LoanPolicy;
import com.library.kernel.web.WebControllerContext;
import com.library.reservation.application.service.reservation.CancelReservationUseCase;
import com.library.reservation.application.service.reservation.CreateReservationUseCase;
import com.library.reservation.application.service.reservation.ExpireReservationsUseCase;
import com.library.reservation.application.service.reservation.FulfillReservationUseCase;
import com.library.reservation.application.service.reservation.GetReservationUseCase;
import com.library.reservation.application.service.reservation.ListReservationsUseCase;
import com.library.reservation.application.service.reservation.NoopPaymentRecorder;
import com.library.reservation.application.service.reservation.RenewReservationUseCase;
import com.library.reservation.application.service.reservation.ReturnReservationUseCase;
import com.library.reservation.application.validation.ReservationValidator;
import com.library.reservation.domain.port.out.PaymentRecorder;
import com.library.reservation.domain.port.out.ReservationRepository;
import com.library.client.infrastructure.loan.ClientLoanPolicy;
import com.library.reservation.infrastructure.notification.ReservationNotificationService;
import com.library.reservation.infrastructure.payment.AccountingPaymentRecorder;
import com.library.reservation.infrastructure.persistence.adapter.ReservationPersistenceAdapter;
import com.library.reservation.infrastructure.persistence.repository.hibernate.HibernateReservationRepository;
import com.library.reservation.infrastructure.web.CatalogRoutes;
import com.library.reservation.infrastructure.web.ReservationRoutes;
import com.library.reservation.infrastructure.web.controller.catalog.CatalogController;
import com.library.reservation.infrastructure.web.controller.catalog.GuestReservationController;
import com.library.reservation.infrastructure.web.controller.reservation.CancelReservationController;
import com.library.reservation.infrastructure.web.controller.reservation.CreateReservationController;
import com.library.reservation.infrastructure.web.controller.reservation.FulfillReservationController;
import com.library.reservation.infrastructure.web.controller.reservation.ListReservationsController;
import com.library.reservation.infrastructure.web.controller.reservation.RenewReservationController;
import com.library.reservation.infrastructure.web.controller.reservation.ReturnReservationController;
import com.library.reservation.infrastructure.web.controller.reservation.ShowReservationController;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.port.out.StockLocationRepository;
import com.library.stock.application.service.stockitem.ListStockItemsUseCase;
import com.library.bootstrap.generation.ShortUuidCodeGenerationService;

import io.javalin.config.JavalinConfig;

public final class ReservationFactory {

    private ReservationFactory() {
    }

    public static void register(JavalinConfig config,
                                SessionFactory sessionFactory,
                                WebControllerContext webContext,
                                StockItemRepository stockItemRepository,
                                StockLocationRepository stockLocationRepository,
                                ClientRepository clientRepository,
                                NotificationService notificationService,
                                PaymentRecorder paymentRecorder) {

        ReservationRepository reservationRepository = new ReservationPersistenceAdapter(
                new HibernateReservationRepository(sessionFactory));

        CodeGenerationService codeGenerationService = new ShortUuidCodeGenerationService();
        LoanPolicy loanPolicy = new ClientLoanPolicy();
        ReservationValidator validator = new ReservationValidator();
        ReservationNotificationService reservationNotificationService = new ReservationNotificationService(notificationService);

        CreateReservationUseCase createReservationUseCase = new CreateReservationUseCase(
                reservationRepository, validator, codeGenerationService, loanPolicy, stockItemRepository, notificationService);
        PaymentRecorder effectiveRecorder = paymentRecorder != null ? paymentRecorder : new NoopPaymentRecorder();

        CancelReservationUseCase cancelReservationUseCase = new CancelReservationUseCase(reservationRepository);
        FulfillReservationUseCase fulfillReservationUseCase = new FulfillReservationUseCase(
                reservationRepository, stockItemRepository, effectiveRecorder);
        ReturnReservationUseCase returnReservationUseCase = new ReturnReservationUseCase(
                reservationRepository, stockItemRepository, effectiveRecorder);
        RenewReservationUseCase renewReservationUseCase = new RenewReservationUseCase(
                reservationRepository, loanPolicy);
        ExpireReservationsUseCase expireReservationsUseCase = new ExpireReservationsUseCase(reservationRepository);
        ListReservationsUseCase listReservationsUseCase = new ListReservationsUseCase(reservationRepository);

        ListClientsUseCase listClientsUseCase = new ListClientsUseCase(clientRepository);
        ListStockItemsUseCase listStockItemsUseCase = new ListStockItemsUseCase(stockItemRepository, stockLocationRepository);
        GetClientByDniUseCase getClientByDniUseCase = new GetClientByDniUseCase(clientRepository);

        EditionRepository editionRepository = new EditionPersistenceAdapter(
                new HibernateEditionRepository(sessionFactory),
                new HibernateEditionAuthorRepository(sessionFactory));
        PublisherRepository publisherRepository = new PublisherPersistenceAdapter(new HibernatePublisherRepository(sessionFactory));
        BookFormatRepository bookFormatRepository = new BookFormatPersistenceAdapter(new HibernateBookFormatRepository(sessionFactory));
        LanguageRepository languageRepository = new LanguagePersistenceAdapter(new HibernateLanguageRepository(sessionFactory));
        WorkRepository workRepository = new WorkPersistenceAdapter(new HibernateWorkRepository(sessionFactory), new HibernateWorkRepository(sessionFactory));

        GetReservationUseCase getReservationUseCase = new GetReservationUseCase(
                reservationRepository, clientRepository, editionRepository, workRepository);

        ListReservationsController listReservationsController = new ListReservationsController(
                listReservationsUseCase, webContext);
        ShowReservationController showReservationController = new ShowReservationController(
                getReservationUseCase, webContext);
        CreateReservationController createReservationController = new CreateReservationController(
                createReservationUseCase, listReservationsUseCase, listClientsUseCase, listStockItemsUseCase, webContext);
        CancelReservationController cancelReservationController = new CancelReservationController(
                cancelReservationUseCase, webContext);
        FulfillReservationController fulfillReservationController = new FulfillReservationController(
                fulfillReservationUseCase, webContext);
        ReturnReservationController returnReservationController = new ReturnReservationController(
                returnReservationUseCase, webContext);
        RenewReservationController renewReservationController = new RenewReservationController(
                renewReservationUseCase, webContext);

        ReservationRoutes.register(config,
                listReservationsController,
                showReservationController,
                createReservationController,
                cancelReservationController,
                fulfillReservationController,
                returnReservationController,
                renewReservationController);

        ListEditionsUseCase listEditionsUseCase = new ListEditionsUseCase(
                editionRepository, workRepository, publisherRepository, bookFormatRepository, languageRepository);
        CreateClientUseCase createClientUseCase = new CreateClientUseCase(clientRepository, null, codeGenerationService);

        CatalogController catalogController = new CatalogController(listEditionsUseCase, stockItemRepository);
        GuestReservationController guestReservationController = new GuestReservationController(
                editionRepository, createReservationUseCase, clientRepository,
                stockItemRepository, loanPolicy);

        CatalogRoutes.register(config, catalogController, guestReservationController);
    }
}
