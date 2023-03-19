package com.ya.pf.transaction;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Page<TransactionEntity> getTransactions(int receiptNumber, int pageNo, int pageSize,
                                                   String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

        if (receiptNumber == -1) {
            return transactionRepository.findAll(pageable);
        } else {
            return transactionRepository.findByReceiptNumberContaining(receiptNumber, pageable);
        }
    }

    @Override
    public TransactionEntity createTransaction(TransactionEntity transactionEntity) {

        transactionEntity.setCreatedDate(new Date());
        transactionEntity.setLastModifiedDate(new Date());
        return transactionRepository.save(transactionEntity);
    }

    @Override
    public void updateTransaction(TransactionEntity transactionEntity) {

        transactionEntity.setLastModifiedDate(new Date());
        transactionRepository.save(transactionEntity);
    }

    @Override
    public void deleteTransactionById(Long id) {

        transactionRepository.deleteById(id);
    }

}
