package com.library.bootstrap.config.hibernate;

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
import com.library.books.infrastructure.persistence.entity.AuthorRoleEntity;
import com.library.books.infrastructure.persistence.entity.envers.BooksRevisionEntity;

public class BooksAnnotatedClases {

    public static void annotate(Configuration cfg){
        cfg.addAnnotatedClass(WorkEntity.class);
        cfg.addAnnotatedClass(EditionEntity.class);
        cfg.addAnnotatedClass(AuthorEntity.class);
        cfg.addAnnotatedClass(PublisherEntity.class);
        cfg.addAnnotatedClass(LanguageEntity.class);
        cfg.addAnnotatedClass(BookFormatEntity.class);
        cfg.addAnnotatedClass(CategoryEntity.class);
        cfg.addAnnotatedClass(WorkAuthorEntity.class);
        cfg.addAnnotatedClass(EditionAuthorEntity.class);
        cfg.addAnnotatedClass(AuthorRoleEntity.class);
        cfg.addAnnotatedClass(BooksRevisionEntity.class);
    }

}
