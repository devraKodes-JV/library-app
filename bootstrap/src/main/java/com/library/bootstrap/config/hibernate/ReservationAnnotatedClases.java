package com.library.bootstrap.config.hibernate;

import org.hibernate.cfg.Configuration;

import com.library.reservation.infrastructure.persistence.entity.ReservationEntity;

public class ReservationAnnotatedClases {

    public static void annotate(Configuration cfg){
        cfg.addAnnotatedClass(ReservationEntity.class);
    }
}
