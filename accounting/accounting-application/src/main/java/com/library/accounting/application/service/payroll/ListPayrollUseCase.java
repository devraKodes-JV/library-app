package com.library.accounting.application.service.payroll;

import com.library.accounting.domain.model.payroll.PayrollPayment;
import com.library.accounting.domain.port.out.PayrollRepository;

import java.util.List;

public class ListPayrollUseCase {

    private final PayrollRepository payrollRepository;

    public ListPayrollUseCase(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    public List<PayrollPayment> execute() {
        return payrollRepository.findAll();
    }
}
