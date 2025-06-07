package com.ya.pf.auditable.expense;

import com.ya.pf.auditable.expense.dto.ExpenseDTO;
import com.ya.pf.auditable.expense.dto.ExpenseDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/expense")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpenseController {

    private final ExpenseService expenseService;

    private final ExpenseDTOMapper expenseDTOMapper;

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getExpenses(@RequestParam long shipmentId) {
        List<ExpenseEntity> expenseEntities = expenseService.getExpenses(shipmentId);
        List<ExpenseDTO> expenseDTOS = expenseEntities.stream().map(expenseDTOMapper).toList();
        return ResponseEntity.ok(expenseDTOS);
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseEntity expense, @RequestParam long paymentMethodId) {
        ExpenseEntity expenseEntity = expenseService.createExpense(expense, paymentMethodId);
        ExpenseDTO expenseDTO = expenseDTOMapper.apply(expenseEntity);
        return ResponseEntity.ok(expenseDTO);
    }

    @PutMapping
    public ResponseEntity<ExpenseDTO> updateExpense(@RequestBody ExpenseEntity expense, @RequestParam long paymentMethodId) {
        ExpenseEntity expenseEntity = expenseService.updateExpense(expense, paymentMethodId);
        ExpenseDTO expenseDTO = expenseDTOMapper.apply(expenseEntity);
        return ResponseEntity.ok(expenseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

}
