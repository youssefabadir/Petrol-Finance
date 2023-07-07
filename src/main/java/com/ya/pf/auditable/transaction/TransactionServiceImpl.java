package com.ya.pf.auditable.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public void deleteTransactionByPaymentId(long paymentId, float paymentAmount) {

        transactionRepository.deleteByPaymentId(paymentId);
    }

    @Override
    public void deleteTransactionByBillId(long billId, float billAmount) {

        transactionRepository.deleteByBillId(billId);
    }

}
