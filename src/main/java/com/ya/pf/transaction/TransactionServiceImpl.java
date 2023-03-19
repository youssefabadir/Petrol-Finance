package com.ya.pf.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final SessionFactory sessionFactory;

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
    public void deleteTransactionById(long id) {

        transactionRepository.deleteById(id);
    }

    @Override
    public String getCustomerReport(long id, int pageNo, int pageSize) throws JsonProcessingException {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize);
        Page<TransactionEntity> page = transactionRepository.findAll(pageable);
        StringBuilder result = new StringBuilder();
        if (page.hasContent()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(page));

            Double totalDueMoney = getColumnSum(id, "dueMoney");
            Double totalPaidMoney = getColumnSum(id, "paidMoney");

            ((ObjectNode) jsonNode).put("totalDueMoney", totalDueMoney);
            ((ObjectNode) jsonNode).put("totalPaidMoney", totalPaidMoney);
            result.append(objectMapper.writeValueAsString(jsonNode));
        }
        return result.toString();
    }

    @Transactional
    public Double getColumnSum(long id, String columnName) {

        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = builder.createQuery(Double.class);
        Root<TransactionEntity> root = criteria.from(TransactionEntity.class);
        criteria.select(builder.sum(root.get(columnName)));
        criteria.where(builder.equal(root.get("customerEntity"), id));
        TypedQuery<Double> query = session.createQuery(criteria);
        Double result = query.getSingleResult();
        session.close();

        return result;
    }

}
