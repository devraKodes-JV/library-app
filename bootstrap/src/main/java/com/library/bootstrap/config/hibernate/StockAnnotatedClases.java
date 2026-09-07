package com.library.bootstrap.config.hibernate;

import org.hibernate.cfg.Configuration;

import com.library.stock.infrastructure.persistence.entity.StockItemEntity;
import com.library.stock.infrastructure.persistence.entity.StockLocationEntity;
import com.library.stock.infrastructure.persistence.entity.StockMovementEntity;

public class StockAnnotatedClases {

    public static void annotate(Configuration cfg) {
        cfg.addAnnotatedClass(StockLocationEntity.class);
        cfg.addAnnotatedClass(StockItemEntity.class);
        cfg.addAnnotatedClass(StockMovementEntity.class);
    }
}
