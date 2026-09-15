package com.library.accounting.infrastructure.persistence.mapper;

import com.library.accounting.application.dto.response.payroll.PayrollResponseDTO;
import com.library.accounting.domain.model.payroll.PayrollPayment;

public class PayrollMapper {

    public static PayrollResponseDTO toDTO(PayrollPayment payroll) {
        return new PayrollResponseDTO(
                payroll.getId(),
                payroll.getCode(),
                payroll.getPaymentDate(),
                payroll.getAmount(),
                payroll.getPaymentMethod(),
                payroll.getStatus() != null ? payroll.getStatus().name() : null,
                payroll.getEmployeeName(),
                payroll.getEmployeeId(),
                payroll.getPeriodMonth(),
                payroll.getPeriodYear(),
                payroll.getNotes()
        );
    }
}
