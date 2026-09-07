package com.library.bootstrap.config.hibernate;

import org.hibernate.cfg.Configuration;

import com.library.accounting.infrastructure.persistence.entity.AccountEntity;
import com.library.accounting.infrastructure.persistence.entity.ExpenseEntity;
import com.library.accounting.infrastructure.persistence.entity.PaymentEntity;
import com.library.accounting.infrastructure.persistence.entity.PayrollPaymentEntity;
import com.library.accounting.infrastructure.persistence.entity.RefundEntity;

public class AccountingAnnotatedClases {

    public static void annotate(Configuration cfg) {
        cfg.addAnnotatedClass(AccountEntity.class);
        cfg.addAnnotatedClass(PaymentEntity.class);
        cfg.addAnnotatedClass(RefundEntity.class);
        cfg.addAnnotatedClass(PayrollPaymentEntity.class);
        cfg.addAnnotatedClass(ExpenseEntity.class);
    }
}
