package com.library.books.infrastructure.persistence.entity.envers;

import org.hibernate.envers.RevisionListener;

import com.library.iam.infrastructure.security.CurrentUser;

public class BooksRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        BooksRevisionEntity entity = (BooksRevisionEntity) revisionEntity;
        entity.setUsername(CurrentUser.getOrDefault("system"));
    }
}
