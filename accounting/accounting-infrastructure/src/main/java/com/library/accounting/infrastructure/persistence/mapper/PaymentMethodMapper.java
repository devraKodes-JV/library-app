package com.library.accounting.infrastructure.persistence.mapper;

import com.library.accounting.domain.model.PaymentMethod;
import com.library.accounting.infrastructure.persistence.entity.PaymentMethodEntity;

public class PaymentMethodMapper {
    public static PaymentMethod toDomain(PaymentMethodEntity entity) {
        if (entity == null) return null;
        return new PaymentMethod(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.isEnabled(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedBy()
        );
    }

    public static PaymentMethodEntity toEntity(PaymentMethod domain) {
        if (domain == null) return null;
        PaymentMethodEntity entity = new PaymentMethodEntity();
        entity.setId(domain.getId());
        entity.setCode(domain.getCode());
        entity.setName(domain.getName());
        entity.setEnabled(domain.isEnabled());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedBy(domain.getUpdatedBy());
        return entity;
    }
}
