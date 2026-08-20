package com.library.books.infrastructure.audit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.hibernate.cfg.Configuration;

import com.library.books.infrastructure.persistence.entity.WorkEntity;
import com.library.books.infrastructure.persistence.entity.EditionEntity;
import com.library.books.infrastructure.persistence.entity.AuthorEntity;
import com.library.books.infrastructure.persistence.entity.PublisherEntity;
import com.library.books.infrastructure.persistence.entity.LanguageEntity;
import com.library.books.infrastructure.persistence.entity.BookFormatEntity;
import com.library.books.infrastructure.persistence.entity.CategoryEntity;
import com.library.books.infrastructure.persistence.entity.WorkAuthorEntity;
import com.library.books.infrastructure.persistence.entity.EditionAuthorEntity;
import com.library.books.infrastructure.persistence.entity.envers.BooksRevisionEntity;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateWorkRepository;

class AuditQueryServiceTest {

    @Test
    void createWork_generatesEnversRevision() {
        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.connection.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        cfg.setProperty("hibernate.connection.username", "sa");
        cfg.setProperty("hibernate.connection.password", "");
        cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        cfg.setProperty("hibernate.show_sql", "false");
        cfg.setProperty("hibernate.format_sql", "true");
        cfg.setProperty("hibernate.audit.enabled", "true");

        cfg.addAnnotatedClass(WorkEntity.class);
        cfg.addAnnotatedClass(EditionEntity.class);
        cfg.addAnnotatedClass(AuthorEntity.class);
        cfg.addAnnotatedClass(PublisherEntity.class);
        cfg.addAnnotatedClass(LanguageEntity.class);
        cfg.addAnnotatedClass(BookFormatEntity.class);
        cfg.addAnnotatedClass(CategoryEntity.class);
        cfg.addAnnotatedClass(WorkAuthorEntity.class);
        cfg.addAnnotatedClass(EditionAuthorEntity.class);
        cfg.addAnnotatedClass(BooksRevisionEntity.class);

        SessionFactory sessionFactory = cfg.buildSessionFactory();

        try {
            HibernateWorkRepository workRepository = new HibernateWorkRepository(sessionFactory);
            AuditQueryService auditQueryService = new AuditQueryService(sessionFactory);

            WorkEntity work = new WorkEntity();
            work.setTitle("Test Work");
            workRepository.save(work);

            List<com.library.books.domain.dto.audit.RevisionInfo> revisions = auditQueryService.getRevisions(WorkEntity.class, work.getId());
            assertFalse(revisions.isEmpty(), "Debe existir al menos una revisión para el work creado");
            assertEquals(1, revisions.size(), "Debe existir exactamente una revisión");
            assertEquals("system", revisions.get(0).username(), "El username por defecto debe ser 'system'");

            com.library.books.domain.dto.audit.EntityAuditEntry entry = auditQueryService.getEntityAtRevision(WorkEntity.class, work.getId(), revisions.get(0).revision());
            assertNotNull(entry.entity());
            assertEquals("Test Work", ((WorkEntity) entry.entity()).getTitle());
        } finally {
            sessionFactory.close();
        }
    }
}
