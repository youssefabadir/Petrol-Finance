package com.ya.pf.auditable.shipment.dto;

import com.ya.pf.auditable.expense.dto.ExpenseDTO;

import java.util.Set;

public record ShipmentDTO(
        long id,
        String billNumber,
        String truckNumber,
        float truckBalance,
        float revenue,
        String note,
        Set<ExpenseDTO> expenses
) {

}
