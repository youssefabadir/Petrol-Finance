package com.ya.pf.auditable.transaction.owner_transaction.entity;

import com.ya.pf.auditable.supplier.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerTransactionServiceImpl implements OwnerTransactionService {

    private final OwnerTransactionRepository ownerTransactionRepository;

    private final SupplierService supplierService;

    @Override
    public void createOwnerTransaction(Long paymentId, Date date) {

        OwnerTransactionEntity ownerTransaction = new OwnerTransactionEntity();
        ownerTransaction.setPaymentId(paymentId);
        ownerTransaction.setDate(date);
        ownerTransactionRepository.save(ownerTransaction);
    }

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
        ownerTransactionRepository.deleteByBillId(billId);
    }

    @Override
    @Transactional
    public void deleteOwnerTransactionByPaymentId(long paymentId, float paymentAmount) {

        ownerTransactionRepository.updateSupplierBalanceByPaymentId(paymentId, Math.abs(paymentAmount) * -1);
        OwnerTransactionEntity ownerTransaction = ownerTransactionRepository.findByPaymentId(paymentId);
        if (ownerTransaction.getSupplierId() != null) {
            supplierService.updateSupplierBalance(ownerTransaction.getSupplierId(), ownerTransaction.getSupplierBalance() - paymentAmount);
        }
        ownerTransactionRepository.deleteByPaymentId(paymentId);
    }

}
