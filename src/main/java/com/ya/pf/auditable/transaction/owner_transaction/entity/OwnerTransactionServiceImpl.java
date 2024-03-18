package com.ya.pf.auditable.transaction.owner_transaction.entity;

import com.ya.pf.auditable.supplier.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
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
    public void createOwnerTransaction(long supplierId, float amount, Long paymentId, Long billId, Date date) {

        float newBalance;
        try {
            newBalance = ownerTransactionRepository.findFirstBySupplierIdAndDateLessThanEqualOrderByDateDescIdDesc(supplierId, date)
                    .getSupplierBalance() + amount;
        } catch (Exception e) {
            newBalance = supplierService.getSupplierById(supplierId).getStartBalance() + amount;
        }
        OwnerTransactionEntity ownerTransaction = new OwnerTransactionEntity();
        ownerTransaction.setSupplierId(supplierId);
        ownerTransaction.setSupplierBalance(newBalance);
        ownerTransaction.setPaymentId(paymentId);
        ownerTransaction.setBillId(billId);
        ownerTransaction.setDate(date);

        ownerTransactionRepository.save(ownerTransaction);

        ownerTransactionRepository.updateSupplierBalanceBySupplierIdAfterDate(amount, supplierId, date);

        newBalance = ownerTransactionRepository.findFirstByOrderByDateDescIdDesc().getSupplierBalance();

        supplierService.updateSupplierBalance(supplierId, newBalance);
    }

    @Override
    public void deleteOwnerTransactionByBillId(long supplierId, long billId, float billAmount, Date date) {

        ownerTransactionRepository.updateSupplierBalanceByBillId(supplierId, billId, billAmount, date);
        OwnerTransactionEntity ownerTransaction = ownerTransactionRepository.findByBillId(billId);
        supplierService.updateSupplierBalance(ownerTransaction.getSupplierId(), ownerTransaction.getSupplierBalance() + billAmount);
        ownerTransactionRepository.deleteByBillId(billId);
    }

    @Override
    public void deleteOwnerTransactionByPaymentId(long supplierId, long paymentId, float paymentAmount, Date date) {

        ownerTransactionRepository.updateSupplierBalanceByPaymentId(supplierId, paymentId, Math.abs(paymentAmount) * -1, date);
        OwnerTransactionEntity ownerTransaction = ownerTransactionRepository.findByPaymentId(paymentId);
        if (ownerTransaction.getSupplierId() != null) {
            supplierService.updateSupplierBalance(ownerTransaction.getSupplierId(), ownerTransaction.getSupplierBalance() - paymentAmount);
        }
        ownerTransactionRepository.deleteByPaymentId(paymentId);
    }

}
