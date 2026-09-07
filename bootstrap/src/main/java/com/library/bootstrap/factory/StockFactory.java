package com.library.bootstrap.factory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;

import com.library.iam.infrastructure.notification.SseNotificationService;
import com.library.security.SecurityFactory;
import com.library.security.service.SecurityAuditService;
import com.library.stock.application.service.stockitem.ChangeStockItemStateUseCase;
import com.library.stock.application.service.stockitem.CreateStockItemUseCase;
import com.library.stock.application.service.stockitem.DeleteStockItemUseCase;
import com.library.stock.application.service.stockitem.GetStockItemUseCase;
import com.library.stock.application.service.stockitem.ListStockItemsUseCase;
import com.library.stock.application.service.stockitem.ReactivateStockItemUseCase;
import com.library.stock.application.service.stockitem.UpdateStockItemUseCase;
import com.library.stock.application.service.stocklocation.CreateStockLocationUseCase;
import com.library.stock.application.service.stocklocation.DeleteStockLocationUseCase;
import com.library.stock.application.service.stocklocation.GetStockLocationUseCase;
import com.library.stock.application.service.stocklocation.ListStockLocationsUseCase;
import com.library.stock.application.service.stocklocation.ReactivateStockLocationUseCase;
import com.library.stock.application.service.stocklocation.UpdateStockLocationUseCase;
import com.library.stock.application.service.stockmovement.CreateStockMovementUseCase;
import com.library.stock.application.service.stockmovement.ListStockMovementsUseCase;
import com.library.books.application.service.edition.ListEditionsUseCase;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateEditionRepository;
import com.library.books.infrastructure.persistence.adapter.EditionPersistenceAdapter;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernatePublisherRepository;
import com.library.books.infrastructure.persistence.adapter.PublisherPersistenceAdapter;
import com.library.books.domain.port.out.PublisherRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateBookFormatRepository;
import com.library.books.infrastructure.persistence.adapter.BookFormatPersistenceAdapter;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateLanguageRepository;
import com.library.books.infrastructure.persistence.adapter.LanguagePersistenceAdapter;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateWorkRepository;
import com.library.books.infrastructure.persistence.adapter.WorkPersistenceAdapter;
import com.library.books.domain.port.out.WorkRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateEditionAuthorRepository;
import com.library.books.infrastructure.persistence.adapter.EditionAuthorPersistenceAdapter;
import com.library.books.domain.port.out.EditionAuthorRepository;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.port.out.StockLocationRepository;
import com.library.stock.domain.port.out.StockMovementRepository;
import com.library.stock.domain.port.out.StockNotificationService;
import com.library.stock.infrastructure.persistence.adapter.StockItemPersistenceAdapter;
import com.library.stock.infrastructure.persistence.adapter.StockLocationPersistenceAdapter;
import com.library.stock.infrastructure.persistence.adapter.StockMovementPersistenceAdapter;
import com.library.stock.infrastructure.persistence.repository.hibernate.HibernateStockItemRepository;
import com.library.stock.infrastructure.persistence.repository.hibernate.HibernateStockLocationRepository;
import com.library.stock.infrastructure.persistence.repository.hibernate.HibernateStockMovementRepository;
import com.library.stock.infrastructure.notification.StockNotificationServiceImpl;
import com.library.stock.infrastructure.web.EditionNamesProvider;
import com.library.stock.infrastructure.web.StockRoutes;
import com.library.stock.infrastructure.web.controller.stockitem.ChangeStockItemStateController;
import com.library.stock.infrastructure.web.controller.stockitem.CreateStockItemController;
import com.library.stock.infrastructure.web.controller.stockitem.DeleteStockItemController;
import com.library.stock.infrastructure.web.controller.stockitem.ListStockItemsController;
import com.library.stock.infrastructure.web.controller.stockitem.ReactivateStockItemController;
import com.library.stock.infrastructure.web.controller.stockitem.ShowStockItemController;
import com.library.stock.infrastructure.web.controller.stockitem.UpdateStockItemController;
import com.library.stock.infrastructure.web.controller.stocklocation.CreateStockLocationController;
import com.library.stock.infrastructure.web.controller.stocklocation.DeleteStockLocationController;
import com.library.stock.infrastructure.web.controller.stocklocation.ListStockLocationsController;
import com.library.stock.infrastructure.web.controller.stocklocation.ReactivateStockLocationController;
import com.library.stock.infrastructure.web.controller.stocklocation.ShowStockLocationController;
import com.library.stock.infrastructure.web.controller.stocklocation.UpdateStockLocationController;
import com.library.kernel.generation.CodeGenerationService;
import com.library.bootstrap.generation.ShortUuidCodeGenerationService;
import com.library.kernel.transaction.Transactional;
import com.library.kernel.web.WebControllerContext;

import io.javalin.config.JavalinConfig;

public final class StockFactory {

    private StockFactory() {
    }

    public static void register(JavalinConfig config,
                                SessionFactory sessionFactory,
                                WebControllerContext webContext,
                                SseNotificationService notificationService) {

        StockItemRepository stockItemRepository = new StockItemPersistenceAdapter(new HibernateStockItemRepository(sessionFactory));
        StockLocationRepository stockLocationRepository = new StockLocationPersistenceAdapter(new HibernateStockLocationRepository(sessionFactory));
        StockMovementRepository stockMovementRepository = new StockMovementPersistenceAdapter(new HibernateStockMovementRepository(sessionFactory));
        EditionRepository editionRepository = new EditionPersistenceAdapter(new HibernateEditionRepository(sessionFactory), new HibernateEditionAuthorRepository(sessionFactory));
        PublisherRepository publisherRepository = new PublisherPersistenceAdapter(new HibernatePublisherRepository(sessionFactory));
        BookFormatRepository bookFormatRepository = new BookFormatPersistenceAdapter(new HibernateBookFormatRepository(sessionFactory));
        LanguageRepository languageRepository = new LanguagePersistenceAdapter(new HibernateLanguageRepository(sessionFactory));
        WorkRepository workRepository = new WorkPersistenceAdapter(new HibernateWorkRepository(sessionFactory), new HibernateWorkRepository(sessionFactory));
        EditionAuthorRepository editionAuthorRepository = new EditionAuthorPersistenceAdapter(new HibernateEditionAuthorRepository(sessionFactory));

        Transactional transactional = new com.library.books.infrastructure.transaction.HibernateTransactionExecutor(sessionFactory);

        CodeGenerationService codeGenerationService = new ShortUuidCodeGenerationService();

        StockNotificationService stockNotificationService = new StockNotificationServiceImpl(notificationService);

        CreateStockItemUseCase createStockItemUseCase = new CreateStockItemUseCase(stockItemRepository, codeGenerationService);
        UpdateStockItemUseCase updateStockItemUseCase = new UpdateStockItemUseCase(stockItemRepository);
        DeleteStockItemUseCase deleteStockItemUseCase = new DeleteStockItemUseCase(stockItemRepository);
        GetStockItemUseCase getStockItemUseCase = new GetStockItemUseCase(stockItemRepository);
        ListStockItemsUseCase listStockItemsUseCase = new ListStockItemsUseCase(stockItemRepository, stockLocationRepository);
        ReactivateStockItemUseCase reactivateStockItemUseCase = new ReactivateStockItemUseCase(stockItemRepository);
        ChangeStockItemStateUseCase changeStockItemStateUseCase = new ChangeStockItemStateUseCase(stockItemRepository, stockNotificationService);

        CreateStockLocationUseCase createStockLocationUseCase = new CreateStockLocationUseCase(stockLocationRepository, codeGenerationService);
        UpdateStockLocationUseCase updateStockLocationUseCase = new UpdateStockLocationUseCase(stockLocationRepository);
        DeleteStockLocationUseCase deleteStockLocationUseCase = new DeleteStockLocationUseCase(stockLocationRepository);
        GetStockLocationUseCase getStockLocationUseCase = new GetStockLocationUseCase(stockLocationRepository);
        ListStockLocationsUseCase listStockLocationsUseCase = new ListStockLocationsUseCase(stockLocationRepository);
        ReactivateStockLocationUseCase reactivateStockLocationUseCase = new ReactivateStockLocationUseCase(stockLocationRepository);

        CreateStockMovementUseCase createStockMovementUseCase = new CreateStockMovementUseCase(stockItemRepository, stockMovementRepository);
        ListStockMovementsUseCase listStockMovementsUseCase = new ListStockMovementsUseCase(stockMovementRepository);

        ListEditionsUseCase listEditionsUseCase = new ListEditionsUseCase(editionRepository, workRepository, publisherRepository, bookFormatRepository, languageRepository);

        EditionNamesProvider editionNamesProvider = new EditionNamesProvider() {
            @Override
            public Map<Long, String> getEditionNames(String status) {
                var editions = listEditionsUseCase.execute(status);
                return editions.stream()
                        .collect(Collectors.toUnmodifiableMap(
                                dto -> dto.id(),
                                dto -> {
                                    String title = dto.workTitle() != null ? dto.workTitle() : "";
                                    String num = dto.editionNumber() != null ? dto.editionNumber() : "";
                                    return title + " - \"" + num + "\"";
                                }));
            }
        };

        ListStockItemsController listStockItemsController = new ListStockItemsController(
                listStockItemsUseCase,
                editionNamesProvider,
                webContext);

        ShowStockItemController showStockItemController = new ShowStockItemController(
                stockItemRepository,
                editionNamesProvider,
                stockLocationRepository,
                webContext);

        CreateStockItemController createStockItemController = new CreateStockItemController(
                createStockItemUseCase,
                editionNamesProvider,
                listStockLocationsUseCase,
                webContext);

        UpdateStockItemController updateStockItemController = new UpdateStockItemController(
                updateStockItemUseCase,
                getStockItemUseCase,
                editionNamesProvider,
                listStockLocationsUseCase,
                webContext);

        DeleteStockItemController deleteStockItemController = new DeleteStockItemController(
                deleteStockItemUseCase,
                webContext);

        ReactivateStockItemController reactivateStockItemController = new ReactivateStockItemController(
                reactivateStockItemUseCase,
                webContext);

        ChangeStockItemStateController changeStockItemStateController = new ChangeStockItemStateController(
                changeStockItemStateUseCase,
                webContext);

        ListStockLocationsController listStockLocationsController = new ListStockLocationsController(
                listStockLocationsUseCase,
                webContext);

        ShowStockLocationController showStockLocationController = new ShowStockLocationController(
                getStockLocationUseCase,
                webContext);

        CreateStockLocationController createStockLocationController = new CreateStockLocationController(
                createStockLocationUseCase,
                webContext);

        UpdateStockLocationController updateStockLocationController = new UpdateStockLocationController(
                updateStockLocationUseCase,
                getStockLocationUseCase,
                webContext);

        DeleteStockLocationController deleteStockLocationController = new DeleteStockLocationController(
                deleteStockLocationUseCase,
                webContext);

        ReactivateStockLocationController reactivateStockLocationController = new ReactivateStockLocationController(
                reactivateStockLocationUseCase,
                webContext);

        SecurityAuditService auditService = SecurityFactory.register(config, sessionFactory);

        StockRoutes.register(config,
                listStockItemsController,
                showStockItemController,
                createStockItemController,
                updateStockItemController,
                deleteStockItemController,
                reactivateStockItemController,
                changeStockItemStateController,
                listStockLocationsController,
                showStockLocationController,
                createStockLocationController,
                updateStockLocationController,
                deleteStockLocationController,
                reactivateStockLocationController,
                notificationService,
                auditService);
    }
}
