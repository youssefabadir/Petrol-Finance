package com.ya.pf.auditable.transaction.owner_transaction.entity;

import com.ya.pf.auditable.supplier.SupplierService;
import com.ya.pf.auditable.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerTransactionServiceImpl implements OwnerTransactionService {

    private final OwnerTransactionRepository ownerTransactionRepository;

    private final TransactionService transactionService;

    private final SupplierService supplierService;

    @Override
    @Transactional
    public void createOwnerTransaction(long supplierId, float amount, Long paymentId, Long billId, Date date) {

        float newBalance = supplierService.getSupplierById(supplierId).getBalance() + amount;

        OwnerTransactionEntity ownerTransaction = new OwnerTransactionEntity();
        ownerTransaction.setSupplierId(supplierId);
        ownerTransaction.setSupplierBalance(newBalance);
        ownerTransaction.setPaymentId(paymentId);
        ownerTransaction.setBillId(billId);
        ownerTransaction.setDate(date);

        ownerTransactionRepository.save(ownerTransaction);

        supplierService.updateSupplierBalance(supplierId, newBalance);
    }

    @Override
    @Transactional
    public void deleteOwnerTransactionByBillId(long billId, float billAmount) {

        ownerTransactionRepository.updateSupplierBalanceByBillId(billId, billAmount);
        OwnerTransactionEntity ownerTransaction = ownerTransactionRepository.findByBillId(billId);
        supplierService.updateSupplierBalance(ownerTransaction.getSupplierId(), ownerTransaction.getSupplierBalance() + billAmount);
        transactionService.deleteTransactionByBillId(billId, billAmount);
    }

    @Override
    @Transactional
    public void deleteOwnerTransactionByPaymentId(long paymentId, float paymentAmount) {

        ownerTransactionRepository.updateSupplierBalanceByPaymentId(paymentId, Math.abs(paymentAmount) * -1);
        OwnerTransactionEntity ownerTransaction = ownerTransactionRepository.findByPaymentId(paymentId);
        supplierService.updateSupplierBalance(ownerTransaction.getSupplierId(), ownerTransaction.getSupplierBalance() - paymentAmount);
        transactionService.deleteTransactionByPaymentId(paymentId, paymentAmount);
    }

}
