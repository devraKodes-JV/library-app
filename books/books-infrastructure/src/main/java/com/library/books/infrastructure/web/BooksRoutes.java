package com.library.books.infrastructure.web;

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
import com.library.books.infrastructure.web.controller.audit.AuditHistoryController;
import com.library.iam.infrastructure.notification.SseNotificationService;
import com.library.security.service.SecurityAuditService;

import io.javalin.config.JavalinConfig;

public final class BooksRoutes {

    private BooksRoutes() {
    }

    public static void register(JavalinConfig config,
                                ListWorksController listWorksController,
                                ShowWorkController showWorkController,
                                CreateWorkController createWorkController,
                                UpdateWorkController updateWorkController,
                                DeleteWorkController deleteWorkController,
                                ListEditionsController listEditionsController,
                                ShowEditionController showEditionController,
                                CreateEditionController createEditionController,
                                UpdateEditionController updateEditionController,
                                DeleteEditionController deleteEditionController,
                                ListAuthorsController listAuthorsController,
                                ShowAuthorController showAuthorController,
                                CreateAuthorController createAuthorController,
                                UpdateAuthorController updateAuthorController,
                                DeleteAuthorController deleteAuthorController,
                                ListPublishersController listPublishersController,
                                ShowPublisherController showPublisherController,
                                CreatePublisherController createPublisherController,
                                UpdatePublisherController updatePublisherController,
                                DeletePublisherController deletePublisherController,
                                ListLanguagesController listLanguagesController,
                                ShowLanguageController showLanguageController,
                                CreateLanguageController createLanguageController,
                                UpdateLanguageController updateLanguageController,
                                DeleteLanguageController deleteLanguageController,
                                ListBookFormatsController listBookFormatsController,
                                ShowBookFormatController showBookFormatController,
                                CreateBookFormatController createBookFormatController,
                                UpdateBookFormatController updateBookFormatController,
                                DeleteBookFormatController deleteBookFormatController,
                                ListCategoriesController listCategoriesController,
                                ShowCategoryController showCategoryController,
                                CreateCategoryController createCategoryController,
                                UpdateCategoryController updateCategoryController,
                                 DeleteCategoryController deleteCategoryController,
                                 ListAuthorRolesController listAuthorRolesController,
                                 ShowAuthorRoleController showAuthorRoleController,
                                 CreateAuthorRoleController createAuthorRoleController,
                                 UpdateAuthorRoleController updateAuthorRoleController,
                                 DeleteAuthorRoleController deleteAuthorRoleController,
                                 AuditHistoryController auditHistoryController,
                                 SseNotificationService notificationService,
                                 SecurityAuditService auditService) {
        config.routes.get("/books/works", listWorksController::listWorks);
        config.routes.get("/books/works/new", createWorkController::showCreateForm);
        config.routes.post("/books/works", createWorkController::createWork);
        config.routes.get("/books/works/{id}", showWorkController::showWork);
        config.routes.get("/books/works/{id}/edit", updateWorkController::showEditForm);
        config.routes.post("/books/works/{id}", updateWorkController::updateWork);
        config.routes.post("/books/works/{id}/delete", deleteWorkController::deleteWork);

        config.routes.get("/books/editions", listEditionsController::listEditions);
        config.routes.get("/books/editions/new", createEditionController::showCreateForm);
        config.routes.post("/books/editions", createEditionController::createEdition);
        config.routes.get("/books/editions/{id}", showEditionController::showEdition);
        config.routes.get("/books/editions/{id}/edit", updateEditionController::showEditForm);
        config.routes.post("/books/editions/{id}", updateEditionController::updateEdition);
        config.routes.post("/books/editions/{id}/delete", deleteEditionController::deleteEdition);

        config.routes.get("/books/authors", listAuthorsController::listAuthors);
        config.routes.get("/books/authors/new", createAuthorController::showCreateForm);
        config.routes.post("/books/authors", createAuthorController::createAuthor);
        config.routes.get("/books/authors/{id}", showAuthorController::showAuthor);
        config.routes.get("/books/authors/{id}/edit", updateAuthorController::showEditForm);
        config.routes.post("/books/authors/{id}", updateAuthorController::updateAuthor);
        config.routes.post("/books/authors/{id}/delete", deleteAuthorController::deleteAuthor);

        config.routes.get("/books/publishers", listPublishersController::listPublishers);
        config.routes.get("/books/publishers/new", createPublisherController::showCreateForm);
        config.routes.post("/books/publishers", createPublisherController::createPublisher);
        config.routes.get("/books/publishers/{id}", showPublisherController::showPublisher);
        config.routes.get("/books/publishers/{id}/edit", updatePublisherController::showEditForm);
        config.routes.post("/books/publishers/{id}", updatePublisherController::updatePublisher);
        config.routes.post("/books/publishers/{id}/delete", deletePublisherController::deletePublisher);

        config.routes.get("/books/languages", listLanguagesController::listLanguages);
        config.routes.get("/books/languages/new", createLanguageController::showCreateForm);
        config.routes.post("/books/languages", createLanguageController::createLanguage);
        config.routes.get("/books/languages/{id}", showLanguageController::showLanguage);
        config.routes.get("/books/languages/{id}/edit", updateLanguageController::showEditForm);
        config.routes.post("/books/languages/{id}", updateLanguageController::updateLanguage);
        config.routes.post("/books/languages/{id}/delete", deleteLanguageController::deleteLanguage);

        config.routes.get("/books/formats", listBookFormatsController::listFormats);
        config.routes.get("/books/formats/new", createBookFormatController::showCreateForm);
        config.routes.post("/books/formats", createBookFormatController::createFormat);
        config.routes.get("/books/formats/{id}", showBookFormatController::showFormat);
        config.routes.get("/books/formats/{id}/edit", updateBookFormatController::showEditForm);
        config.routes.post("/books/formats/{id}", updateBookFormatController::updateFormat);
        config.routes.post("/books/formats/{id}/delete", deleteBookFormatController::deleteFormat);

        config.routes.get("/books/categories", listCategoriesController::listCategories);
        config.routes.get("/books/categories/new", createCategoryController::showCreateForm);
        config.routes.post("/books/categories", createCategoryController::createCategory);
        config.routes.get("/books/categories/{id}", showCategoryController::showCategory);
        config.routes.get("/books/categories/{id}/edit", updateCategoryController::showEditForm);
        config.routes.post("/books/categories/{id}", updateCategoryController::updateCategory);
        config.routes.post("/books/categories/{id}/delete", deleteCategoryController::deleteCategory);

        config.routes.get("/books/authorRoles", listAuthorRolesController::listAuthorRoles);
        config.routes.get("/books/authorRoles/new", createAuthorRoleController::showCreateForm);
        config.routes.post("/books/authorRoles", createAuthorRoleController::createAuthorRole);
        config.routes.get("/books/authorRoles/{id}", showAuthorRoleController::showAuthorRole);
        config.routes.get("/books/authorRoles/{id}/edit", updateAuthorRoleController::showEditForm);
        config.routes.post("/books/authorRoles/{id}", updateAuthorRoleController::updateAuthorRole);
        config.routes.post("/books/authorRoles/{id}/delete", deleteAuthorRoleController::deleteAuthorRole);

        config.routes.get("/books/audit/revisions", auditHistoryController::showRevisions);
        config.routes.get("/books/audit/entity", auditHistoryController::showEntityAtRevision);
    }
}
