package com.library.bootstrap.config.hibernate;

import org.hibernate.cfg.Configuration;

import com.library.client.infrastructure.persistence.entity.ClientEntity;

public class ClientAnnotatedClases {

    public static void annotate(Configuration cfg){
        cfg.addAnnotatedClass(ClientEntity.class);
    }
}
