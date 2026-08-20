package com.library.books.infrastructure.audit;

import java.time.Instant;
import java.util.List;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.Session;

import com.library.books.domain.dto.audit.EntityAuditEntry;
import com.library.books.domain.dto.audit.RevisionInfo;
import com.library.books.infrastructure.persistence.entity.envers.BooksRevisionEntity;
import com.library.iam.infrastructure.security.CurrentUser;

public class AuditQueryService {

    private final org.hibernate.SessionFactory sessionFactory;

    public AuditQueryService(org.hibernate.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<RevisionInfo> getRevisions(Class<?> entityClass, Long entityId) {
        try (Session session = sessionFactory.openSession()) {
            AuditReader auditReader = AuditReaderFactory.get(session);
            List<Number> revisionNumbers = auditReader.getRevisions(entityClass, entityId);

            return revisionNumbers.stream()
                    .map(rev -> {
                        BooksRevisionEntity revisionEntity = session.get(BooksRevisionEntity.class, rev.longValue());
                        Instant timestamp = Instant.ofEpochMilli(revisionEntity.getTimestamp());
                        String username = revisionEntity.getUsername() != null ? revisionEntity.getUsername() : CurrentUser.getOrDefault("system");
                        return new RevisionInfo(rev.longValue(), timestamp, username);
                    })
                    .toList();
        }
    }

    public <T> EntityAuditEntry getEntityAtRevision(Class<T> entityClass, Long entityId, long revision) {
        try (Session session = sessionFactory.openSession()) {
            AuditReader auditReader = AuditReaderFactory.get(session);
            T entity = auditReader.find(entityClass, entityId, revision);

            AuditQuery auditQuery = auditReader.createQuery()
                    .forRevisionsOfEntity(entityClass, false, true)
                    .add(AuditEntity.id().eq(entityId))
                    .add(AuditEntity.revisionNumber().eq(revision));

            List<Object[]> results = auditQuery.getResultList();
            int revisionType = results.isEmpty() ? 0 : toRevisionType(results.get(0)[2]);

            BooksRevisionEntity revisionEntity = session.get(BooksRevisionEntity.class, revision);
            Instant timestamp = Instant.ofEpochMilli(revisionEntity.getTimestamp());
            String username = revisionEntity.getUsername() != null ? revisionEntity.getUsername() : CurrentUser.getOrDefault("system");

            return new EntityAuditEntry(revision, timestamp, username, revisionType, entity);
        }
    }

    private static int toRevisionType(Object value) {
        if (value instanceof RevisionType type) {
            return switch (type) {
                case ADD -> 0;
                case MOD -> 1;
                case DEL -> 2;
            };
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return 0;
    }
}
