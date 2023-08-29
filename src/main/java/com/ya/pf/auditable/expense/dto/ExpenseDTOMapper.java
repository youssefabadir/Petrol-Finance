package com.ya.pf.auditable.expense.dto;

import com.ya.pf.auditable.expense.ExpenseEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ExpenseDTOMapper implements Function<ExpenseEntity, ExpenseDTO> {

    @Override
    public ExpenseDTO apply(ExpenseEntity expenseEntity) {

        return new ExpenseDTO(expenseEntity.getId(), expenseEntity.getAmount(), expenseEntity.getNote());
    }

}
