package com.library.bootstrap.config.hibernate;

import org.hibernate.cfg.Configuration;

import com.library.iam.infrastructure.persistence.entity.ModuleEntity;
import com.library.iam.infrastructure.persistence.entity.PermissionEntity;
import com.library.iam.infrastructure.persistence.entity.RoleEntity;
import com.library.iam.infrastructure.persistence.entity.UserEntity;
import com.library.security.persistence.entity.SecurityAuditEventEntity;

public class IamAnnotatedClases {

    public static void annotate(Configuration cfg){
        cfg.addAnnotatedClass(UserEntity.class);
        cfg.addAnnotatedClass(RoleEntity.class);
        cfg.addAnnotatedClass(PermissionEntity.class);
        cfg.addAnnotatedClass(ModuleEntity.class);
        cfg.addAnnotatedClass(SecurityAuditEventEntity.class);
    }

}
