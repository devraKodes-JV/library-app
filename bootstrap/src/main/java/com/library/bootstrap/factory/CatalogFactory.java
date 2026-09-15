package com.library.bootstrap.factory;

import org.hibernate.SessionFactory;

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
import com.library.catalog.infrastructure.web.CatalogRoutes;
import com.library.catalog.infrastructure.web.controller.CatalogController;
import com.library.stock.domain.port.out.StockItemRepository;

import io.javalin.config.JavalinConfig;

public final class CatalogFactory {

    private CatalogFactory() {
    }

    public static void register(JavalinConfig config,
                                SessionFactory sessionFactory,
                                StockItemRepository stockItemRepository) {

        EditionRepository editionRepository = new EditionPersistenceAdapter(
                new HibernateEditionRepository(sessionFactory),
                new HibernateEditionAuthorRepository(sessionFactory));
        PublisherRepository publisherRepository = new PublisherPersistenceAdapter(
                new HibernatePublisherRepository(sessionFactory));
        BookFormatRepository bookFormatRepository = new BookFormatPersistenceAdapter(
                new HibernateBookFormatRepository(sessionFactory));
        LanguageRepository languageRepository = new LanguagePersistenceAdapter(
                new HibernateLanguageRepository(sessionFactory));
        WorkRepository workRepository = new WorkPersistenceAdapter(
                new HibernateWorkRepository(sessionFactory), new HibernateWorkRepository(sessionFactory));

        ListEditionsUseCase listEditionsUseCase = new ListEditionsUseCase(
                editionRepository, workRepository, publisherRepository,
                bookFormatRepository, languageRepository);

        CatalogController catalogController = new CatalogController(listEditionsUseCase, stockItemRepository);
        CatalogRoutes.register(config, catalogController);
    }
}
