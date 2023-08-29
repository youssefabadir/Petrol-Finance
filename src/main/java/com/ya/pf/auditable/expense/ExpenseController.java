package com.ya.pf.auditable.expense;

import com.ya.pf.auditable.expense.dto.ExpenseDTO;
import com.ya.pf.auditable.expense.dto.ExpenseDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
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

        try {
            List<ExpenseEntity> expenseEntities = expenseService.getExpenses(shipmentId);
            List<ExpenseDTO> expenseDTOS = expenseEntities.stream().map(expenseDTOMapper).toList();
            return ResponseEntity.ok(expenseDTOS);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseEntity expense,
                                                    @RequestParam(defaultValue = "") String paymentNumber) {

        try {
            ExpenseEntity expenseEntity = expenseService.createExpense(expense, paymentNumber);
            ExpenseDTO expenseDTO = expenseDTOMapper.apply(expenseEntity);
            return ResponseEntity.ok(expenseDTO);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<ExpenseDTO> updateExpense(@RequestBody ExpenseEntity expense,
                                                    @RequestParam(defaultValue = "") String paymentNumber) {

        try {
            ExpenseEntity expenseEntity = expenseService.updateExpense(expense, paymentNumber);
            ExpenseDTO expenseDTO = expenseDTOMapper.apply(expenseEntity);
            return ResponseEntity.ok(expenseDTO);
        } catch (EntityNotFoundException e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable long id) {

        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
