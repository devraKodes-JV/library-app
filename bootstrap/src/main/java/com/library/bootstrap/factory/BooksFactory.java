package com.library.bootstrap.factory;

import org.hibernate.SessionFactory;

import com.library.security.SecurityFactory;
import com.library.books.application.service.author.CreateAuthorUseCase;
import com.library.books.application.service.author.DeleteAuthorUseCase;
import com.library.books.application.service.author.GetAuthorDetailUseCase;
import com.library.books.application.service.author.GetAuthorUseCase;
import com.library.books.application.service.author.ListAuthorsUseCase;
import com.library.books.application.service.author.UpdateAuthorUseCase;
import com.library.books.application.service.authorRole.ListAuthorRolesUseCase;
import com.library.books.application.service.bookFormat.CreateBookFormatUseCase;
import com.library.books.application.service.bookFormat.DeleteBookFormatUseCase;
import com.library.books.application.service.bookFormat.GetBookFormatUseCase;
import com.library.books.application.service.bookFormat.ListBookFormatsUseCase;
import com.library.books.application.service.bookFormat.UpdateBookFormatUseCase;
import com.library.books.application.service.category.CreateCategoryUseCase;
import com.library.books.application.service.category.DeleteCategoryUseCase;
import com.library.books.application.service.category.GetCategoryUseCase;
import com.library.books.application.service.category.ListCategoriesUseCase;
import com.library.books.application.service.category.UpdateCategoryUseCase;
import com.library.books.application.service.edition.CreateEditionUseCase;
import com.library.books.application.service.edition.DeleteEditionUseCase;
import com.library.books.application.service.edition.GetEditionUseCase;
import com.library.books.application.service.edition.ListEditionsByFormatUseCase;
import com.library.books.application.service.edition.ListEditionsByPublisherUseCase;
import com.library.books.application.service.edition.ListEditionsByWorkUseCase;
import com.library.books.application.service.edition.ListEditionsUseCase;
import com.library.books.application.service.edition.UpdateEditionUseCase;
import com.library.books.application.service.language.CreateLanguageUseCase;
import com.library.books.application.service.language.DeleteLanguageUseCase;
import com.library.books.application.service.language.GetLanguageUseCase;
import com.library.books.application.service.language.ListLanguagesUseCase;
import com.library.books.application.service.language.UpdateLanguageUseCase;
import com.library.books.application.service.publisher.CreatePublisherUseCase;
import com.library.books.application.service.publisher.DeletePublisherUseCase;
import com.library.books.application.service.publisher.GetPublisherUseCase;
import com.library.books.application.service.publisher.ListPublishersUseCase;
import com.library.books.application.service.publisher.UpdatePublisherUseCase;
import com.library.books.application.service.work.CreateWorkUseCase;
import com.library.books.application.service.work.DeleteWorkUseCase;
import com.library.books.application.service.work.GetWorkDetailUseCase;
import com.library.books.application.service.work.GetWorkUseCase;
import com.library.books.application.service.work.ListWorksByAuthorUseCase;
import com.library.books.application.service.work.ListWorksByCategoryUseCase;
import com.library.books.application.service.work.ListWorksByLanguageUseCase;
import com.library.books.application.service.work.ListWorksUseCase;
import com.library.books.application.service.work.UpdateWorkUseCase;
import com.library.books.application.service.authorRole.CreateAuthorRoleUseCase;
import com.library.books.application.service.authorRole.DeleteAuthorRoleUseCase;
import com.library.books.application.service.authorRole.GetAuthorRoleUseCase;
import com.library.books.application.service.authorRole.UpdateAuthorRoleUseCase;
import com.library.books.application.validation.AuthorValidator;
import com.library.books.application.validation.AuthorRoleValidator;
import com.library.books.application.validation.BookFormatValidator;
import com.library.books.application.validation.CategoryValidator;
import com.library.books.application.validation.EditionValidator;
import com.library.books.application.validation.LanguageValidator;
import com.library.books.application.validation.PublisherValidator;
import com.library.books.application.validation.WorkValidator;
import com.library.books.infrastructure.web.controller.author.CreateAuthorController;
import com.library.books.infrastructure.web.controller.author.DeleteAuthorController;
import com.library.books.infrastructure.web.controller.author.ListAuthorsController;
import com.library.books.infrastructure.web.controller.author.ShowAuthorController;
import com.library.books.infrastructure.web.controller.author.UpdateAuthorController;
import com.library.books.infrastructure.web.controller.bookFormat.CreateBookFormatController;
import com.library.books.infrastructure.web.controller.bookFormat.DeleteBookFormatController;
import com.library.books.infrastructure.web.controller.bookFormat.ListBookFormatsController;
import com.library.books.infrastructure.web.controller.bookFormat.ShowBookFormatController;
import com.library.books.infrastructure.web.controller.bookFormat.UpdateBookFormatController;
import com.library.books.infrastructure.web.controller.category.CreateCategoryController;
import com.library.books.infrastructure.web.controller.category.DeleteCategoryController;
import com.library.books.infrastructure.web.controller.category.ListCategoriesController;
import com.library.books.infrastructure.web.controller.category.ShowCategoryController;
import com.library.books.infrastructure.web.controller.category.UpdateCategoryController;
import com.library.books.infrastructure.web.controller.authorRole.CreateAuthorRoleController;
import com.library.books.infrastructure.web.controller.authorRole.DeleteAuthorRoleController;
import com.library.books.infrastructure.web.controller.authorRole.ListAuthorRolesController;
import com.library.books.infrastructure.web.controller.authorRole.ShowAuthorRoleController;
import com.library.books.infrastructure.web.controller.authorRole.UpdateAuthorRoleController;
import com.library.books.infrastructure.web.controller.edition.CreateEditionController;
import com.library.books.infrastructure.web.controller.edition.DeleteEditionController;
import com.library.books.infrastructure.web.controller.edition.ListEditionsController;
import com.library.books.infrastructure.web.controller.edition.ShowEditionController;
import com.library.books.infrastructure.web.controller.edition.UpdateEditionController;
import com.library.books.infrastructure.web.controller.language.CreateLanguageController;
import com.library.books.infrastructure.web.controller.language.DeleteLanguageController;
import com.library.books.infrastructure.web.controller.language.ListLanguagesController;
import com.library.books.infrastructure.web.controller.language.ShowLanguageController;
import com.library.books.infrastructure.web.controller.language.UpdateLanguageController;
import com.library.books.infrastructure.web.controller.publisher.CreatePublisherController;
import com.library.books.infrastructure.web.controller.publisher.DeletePublisherController;
import com.library.books.infrastructure.web.controller.publisher.ListPublishersController;
import com.library.books.infrastructure.web.controller.publisher.ShowPublisherController;
import com.library.books.infrastructure.web.controller.publisher.UpdatePublisherController;
import com.library.books.infrastructure.web.controller.work.CreateWorkController;
import com.library.books.infrastructure.web.controller.work.DeleteWorkController;
import com.library.books.infrastructure.web.controller.work.ListWorksController;
import com.library.books.infrastructure.web.controller.work.ShowWorkController;
import com.library.books.infrastructure.web.controller.work.UpdateWorkController;
import com.library.books.domain.port.out.AuthorRepository;
import com.library.books.domain.port.out.AuthorRoleRepository;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.books.domain.port.out.CategoryRepository;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.EditionAuthorRepository;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.domain.port.out.PublisherRepository;
import com.library.books.domain.port.out.WorkAuthorRepository;
import com.library.books.domain.port.out.WorkRepository;
import com.library.books.infrastructure.persistence.adapter.AuthorPersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.AuthorRolePersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.BookFormatPersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.CategoryPersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.EditionPersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.EditionAuthorPersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.LanguagePersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.PublisherPersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.WorkAuthorPersistenceAdapter;
import com.library.books.infrastructure.persistence.adapter.WorkPersistenceAdapter;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateAuthorRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateAuthorRoleRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateBookFormatRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateCategoryRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateEditionRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateEditionAuthorRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateLanguageRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernatePublisherRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateWorkAuthorRepository;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateWorkRepository;
import com.library.books.infrastructure.audit.AuditQueryService;
import com.library.books.infrastructure.web.BooksRoutes;
import com.library.books.infrastructure.web.controller.audit.AuditHistoryController;
import com.library.kernel.web.WebHelper;
import com.library.iam.infrastructure.notification.SseNotificationService;
import com.library.kernel.transaction.Transactional;
import com.library.kernel.web.WebControllerContext;
import com.library.security.service.SecurityAuditService;

import io.javalin.config.JavalinConfig;

public final class BooksFactory {

    private BooksFactory() {
    }

    public static void register(JavalinConfig config, SessionFactory sessionFactory, WebControllerContext webContext) {
        WorkRepository workRepository = new WorkPersistenceAdapter(new HibernateWorkRepository(sessionFactory), new HibernateWorkRepository(sessionFactory));
        EditionRepository editionRepository = new EditionPersistenceAdapter(new HibernateEditionRepository(sessionFactory), new HibernateEditionAuthorRepository(sessionFactory));
        AuthorRepository authorRepository = new AuthorPersistenceAdapter(new HibernateAuthorRepository(sessionFactory), new HibernateAuthorRepository(sessionFactory));
        PublisherRepository publisherRepository = new PublisherPersistenceAdapter(new HibernatePublisherRepository(sessionFactory));
        LanguageRepository languageRepository = new LanguagePersistenceAdapter(new HibernateLanguageRepository(sessionFactory));
        BookFormatRepository bookFormatRepository = new BookFormatPersistenceAdapter(new HibernateBookFormatRepository(sessionFactory));
        CategoryRepository categoryRepository = new CategoryPersistenceAdapter(new HibernateCategoryRepository(sessionFactory));
        WorkAuthorRepository workAuthorRepository = new WorkAuthorPersistenceAdapter(new HibernateWorkAuthorRepository(sessionFactory));
        EditionAuthorRepository editionAuthorRepository = new EditionAuthorPersistenceAdapter(new HibernateEditionAuthorRepository(sessionFactory));
        AuthorRoleRepository authorRoleRepository = new AuthorRolePersistenceAdapter(new HibernateAuthorRoleRepository(sessionFactory));

        Transactional transactional = new com.library.books.infrastructure.transaction.HibernateTransactionExecutor(sessionFactory);
        AuthorValidator authorValidator = new AuthorValidator();
        BookFormatValidator bookFormatValidator = new BookFormatValidator();
        CategoryValidator categoryValidator = new CategoryValidator();
        EditionValidator editionValidator = new EditionValidator();
        LanguageValidator languageValidator = new LanguageValidator();
        PublisherValidator publisherValidator = new PublisherValidator();
        WorkValidator workValidator = new WorkValidator();
        AuthorRoleValidator authorRoleValidator = new AuthorRoleValidator();

        CreateAuthorUseCase createAuthorUseCase = new CreateAuthorUseCase(authorRepository, authorValidator);
        UpdateAuthorUseCase updateAuthorUseCase = new UpdateAuthorUseCase(authorRepository, authorValidator);
        DeleteAuthorUseCase deleteAuthorUseCase = new DeleteAuthorUseCase(authorRepository);
        GetAuthorUseCase getAuthorUseCase = new GetAuthorUseCase(authorRepository);
        ListAuthorsUseCase listAuthorsUseCase = new ListAuthorsUseCase(authorRepository);
        GetAuthorDetailUseCase getAuthorDetailUseCase = new GetAuthorDetailUseCase(authorRepository);

        CreateBookFormatUseCase createBookFormatUseCase = new CreateBookFormatUseCase(bookFormatRepository, bookFormatValidator);
        UpdateBookFormatUseCase updateBookFormatUseCase = new UpdateBookFormatUseCase(bookFormatRepository, bookFormatValidator);
        DeleteBookFormatUseCase deleteBookFormatUseCase = new DeleteBookFormatUseCase(bookFormatRepository, editionRepository);
        GetBookFormatUseCase getBookFormatUseCase = new GetBookFormatUseCase(bookFormatRepository);
        ListBookFormatsUseCase listBookFormatsUseCase = new ListBookFormatsUseCase(bookFormatRepository);

        CreateCategoryUseCase createCategoryUseCase = new CreateCategoryUseCase(categoryRepository, categoryValidator);
        UpdateCategoryUseCase updateCategoryUseCase = new UpdateCategoryUseCase(categoryRepository, categoryValidator);
        DeleteCategoryUseCase deleteCategoryUseCase = new DeleteCategoryUseCase(categoryRepository, workRepository);
        GetCategoryUseCase getCategoryUseCase = new GetCategoryUseCase(categoryRepository);
        ListCategoriesUseCase listCategoriesUseCase = new ListCategoriesUseCase(categoryRepository);

        CreateEditionUseCase createEditionUseCase = new CreateEditionUseCase(editionRepository, editionValidator, transactional, workRepository, publisherRepository, bookFormatRepository, languageRepository, editionAuthorRepository);
        UpdateEditionUseCase updateEditionUseCase = new UpdateEditionUseCase(editionRepository, editionValidator, publisherRepository, bookFormatRepository, languageRepository, editionAuthorRepository);
        DeleteEditionUseCase deleteEditionUseCase = new DeleteEditionUseCase(editionRepository);
        GetEditionUseCase getEditionUseCase = new GetEditionUseCase(editionRepository, workRepository, publisherRepository, bookFormatRepository, languageRepository);
        ListEditionsUseCase listEditionsUseCase = new ListEditionsUseCase(editionRepository);
        ListEditionsByPublisherUseCase listEditionsByPublisherUseCase = new ListEditionsByPublisherUseCase(editionRepository);
        ListEditionsByFormatUseCase listEditionsByFormatUseCase = new ListEditionsByFormatUseCase(editionRepository);
        ListEditionsByWorkUseCase listEditionsByWorkUseCase = new ListEditionsByWorkUseCase(editionRepository);

        CreateLanguageUseCase createLanguageUseCase = new CreateLanguageUseCase(languageRepository, languageValidator);
        UpdateLanguageUseCase updateLanguageUseCase = new UpdateLanguageUseCase(languageRepository, languageValidator);
        DeleteLanguageUseCase deleteLanguageUseCase = new DeleteLanguageUseCase(languageRepository, editionRepository, workRepository);
        GetLanguageUseCase getLanguageUseCase = new GetLanguageUseCase(languageRepository);
        ListLanguagesUseCase listLanguagesUseCase = new ListLanguagesUseCase(languageRepository);

        CreatePublisherUseCase createPublisherUseCase = new CreatePublisherUseCase(publisherRepository, publisherValidator);
        UpdatePublisherUseCase updatePublisherUseCase = new UpdatePublisherUseCase(publisherRepository, publisherValidator);
        DeletePublisherUseCase deletePublisherUseCase = new DeletePublisherUseCase(publisherRepository, editionRepository);
        GetPublisherUseCase getPublisherUseCase = new GetPublisherUseCase(publisherRepository);
        ListPublishersUseCase listPublishersUseCase = new ListPublishersUseCase(publisherRepository);

        CreateWorkUseCase createWorkUseCase = new CreateWorkUseCase(workRepository, authorRepository, languageRepository, categoryRepository, workValidator, transactional);
        UpdateWorkUseCase updateWorkUseCase = new UpdateWorkUseCase(workRepository, authorRepository, languageRepository, categoryRepository, workValidator, transactional);
        DeleteWorkUseCase deleteWorkUseCase = new DeleteWorkUseCase(workRepository, editionRepository);
        GetWorkUseCase getWorkUseCase = new GetWorkUseCase(workRepository);
        ListWorksUseCase listWorksUseCase = new ListWorksUseCase(workRepository, languageRepository, categoryRepository);
        ListWorksByCategoryUseCase listWorksByCategoryUseCase = new ListWorksByCategoryUseCase(workRepository, languageRepository, categoryRepository);
        ListWorksByLanguageUseCase listWorksByLanguageUseCase = new ListWorksByLanguageUseCase(workRepository, languageRepository, categoryRepository);
        ListWorksByAuthorUseCase listWorksByAuthorUseCase = new ListWorksByAuthorUseCase(workRepository, languageRepository, categoryRepository);
        GetWorkDetailUseCase getWorkDetailUseCase = new GetWorkDetailUseCase(workRepository);

        CreateAuthorRoleUseCase createAuthorRoleUseCase = new CreateAuthorRoleUseCase(authorRoleRepository);
        UpdateAuthorRoleUseCase updateAuthorRoleUseCase = new UpdateAuthorRoleUseCase(authorRoleRepository);
        DeleteAuthorRoleUseCase deleteAuthorRoleUseCase = new DeleteAuthorRoleUseCase(authorRoleRepository);
        GetAuthorRoleUseCase getAuthorRoleUseCase = new GetAuthorRoleUseCase(authorRoleRepository);

        SecurityAuditService auditService = SecurityFactory.register(config, sessionFactory);
        AuditQueryService auditQueryService = new AuditQueryService(sessionFactory);
        SseNotificationService notificationService = new SseNotificationService();

        ListAuthorRolesUseCase listAuthorRolesUseCase = new ListAuthorRolesUseCase(authorRoleRepository);

        ListWorksController listWorksController = new ListWorksController(listWorksUseCase, webContext);
        ShowWorkController showWorkController = new ShowWorkController(getWorkDetailUseCase, listEditionsByWorkUseCase, webContext);
        CreateWorkController createWorkController = new CreateWorkController(createWorkUseCase, listWorksUseCase, listLanguagesUseCase, listCategoriesUseCase, listAuthorsUseCase, listAuthorRolesUseCase, webContext);
        UpdateWorkController updateWorkController = new UpdateWorkController(updateWorkUseCase, getWorkUseCase, listWorksUseCase, listLanguagesUseCase, listCategoriesUseCase, listAuthorsUseCase, listAuthorRolesUseCase, webContext);
        DeleteWorkController deleteWorkController = new DeleteWorkController(deleteWorkUseCase, webContext);

        ListEditionsController listEditionsController = new ListEditionsController(listEditionsUseCase, webContext);
        ShowEditionController showEditionController = new ShowEditionController(getEditionUseCase, listAuthorsUseCase, listAuthorRolesUseCase, webContext);
        CreateEditionController createEditionController = new CreateEditionController(createEditionUseCase, listWorksUseCase, listPublishersUseCase, listBookFormatsUseCase, listLanguagesUseCase, listAuthorsUseCase, listAuthorRolesUseCase, webContext);
        UpdateEditionController updateEditionController = new UpdateEditionController(updateEditionUseCase, getEditionUseCase, listWorksUseCase, listPublishersUseCase, listBookFormatsUseCase, listLanguagesUseCase, listAuthorsUseCase, listAuthorRolesUseCase, webContext);
        DeleteEditionController deleteEditionController = new DeleteEditionController(deleteEditionUseCase, webContext);

        ListAuthorsController listAuthorsController = new ListAuthorsController(listAuthorsUseCase, webContext);
        ShowAuthorController showAuthorController = new ShowAuthorController(getAuthorDetailUseCase, webContext);
        CreateAuthorController createAuthorController = new CreateAuthorController(createAuthorUseCase, webContext);
        UpdateAuthorController updateAuthorController = new UpdateAuthorController(updateAuthorUseCase, getAuthorUseCase, webContext);
        DeleteAuthorController deleteAuthorController = new DeleteAuthorController(deleteAuthorUseCase, webContext);

        ListPublishersController listPublishersController = new ListPublishersController(listPublishersUseCase, webContext);
        ShowPublisherController showPublisherController = new ShowPublisherController(getPublisherUseCase, listEditionsByPublisherUseCase, webContext);
        CreatePublisherController createPublisherController = new CreatePublisherController(createPublisherUseCase, webContext);
        UpdatePublisherController updatePublisherController = new UpdatePublisherController(updatePublisherUseCase, getPublisherUseCase, webContext);
        DeletePublisherController deletePublisherController = new DeletePublisherController(deletePublisherUseCase, webContext);

        ListLanguagesController listLanguagesController = new ListLanguagesController(listLanguagesUseCase, webContext);
        ShowLanguageController showLanguageController = new ShowLanguageController(getLanguageUseCase, listWorksByLanguageUseCase, webContext);
        CreateLanguageController createLanguageController = new CreateLanguageController(createLanguageUseCase, webContext);
        UpdateLanguageController updateLanguageController = new UpdateLanguageController(updateLanguageUseCase, getLanguageUseCase, webContext);
        DeleteLanguageController deleteLanguageController = new DeleteLanguageController(deleteLanguageUseCase, webContext);

        ListBookFormatsController listBookFormatsController = new ListBookFormatsController(listBookFormatsUseCase, webContext);
        ShowBookFormatController showBookFormatController = new ShowBookFormatController(getBookFormatUseCase, listEditionsByFormatUseCase, webContext);
        CreateBookFormatController createBookFormatController = new CreateBookFormatController(createBookFormatUseCase, webContext);
        UpdateBookFormatController updateBookFormatController = new UpdateBookFormatController(updateBookFormatUseCase, getBookFormatUseCase, webContext);
        DeleteBookFormatController deleteBookFormatController = new DeleteBookFormatController(deleteBookFormatUseCase, webContext);

        ListCategoriesController listCategoriesController = new ListCategoriesController(listCategoriesUseCase, webContext);
        ShowCategoryController showCategoryController = new ShowCategoryController(getCategoryUseCase, listCategoriesUseCase, listWorksByCategoryUseCase, webContext);
        CreateCategoryController createCategoryController = new CreateCategoryController(createCategoryUseCase, listCategoriesUseCase, webContext);
        UpdateCategoryController updateCategoryController = new UpdateCategoryController(updateCategoryUseCase, getCategoryUseCase, listCategoriesUseCase, webContext);
        DeleteCategoryController deleteCategoryController = new DeleteCategoryController(deleteCategoryUseCase, webContext);

        ListAuthorRolesController listAuthorRolesController = new ListAuthorRolesController(listAuthorRolesUseCase, webContext);
        ShowAuthorRoleController showAuthorRoleController = new ShowAuthorRoleController(getAuthorRoleUseCase, webContext);
        CreateAuthorRoleController createAuthorRoleController = new CreateAuthorRoleController(createAuthorRoleUseCase, webContext);
        UpdateAuthorRoleController updateAuthorRoleController = new UpdateAuthorRoleController(updateAuthorRoleUseCase, getAuthorRoleUseCase, webContext);
        DeleteAuthorRoleController deleteAuthorRoleController = new DeleteAuthorRoleController(deleteAuthorRoleUseCase, webContext);

        AuditHistoryController auditHistoryController = new AuditHistoryController(auditQueryService, webContext);

        BooksRoutes.register(config,
                listWorksController, showWorkController, createWorkController, updateWorkController, deleteWorkController,
                listEditionsController, showEditionController, createEditionController, updateEditionController, deleteEditionController,
                listAuthorsController, showAuthorController, createAuthorController, updateAuthorController, deleteAuthorController,
                listPublishersController, showPublisherController, createPublisherController, updatePublisherController, deletePublisherController,
                listLanguagesController, showLanguageController, createLanguageController, updateLanguageController, deleteLanguageController,
                listBookFormatsController, showBookFormatController, createBookFormatController, updateBookFormatController, deleteBookFormatController,
                listCategoriesController, showCategoryController, createCategoryController, updateCategoryController, deleteCategoryController,
                listAuthorRolesController, showAuthorRoleController, createAuthorRoleController, updateAuthorRoleController, deleteAuthorRoleController,
                auditHistoryController,
                notificationService,
                auditService);
    }
}
