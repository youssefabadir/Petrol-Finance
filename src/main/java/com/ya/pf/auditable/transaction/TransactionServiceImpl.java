package com.ya.pf.auditable.transaction;

import com.ya.pf.auditable.product.ProductService;
import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;

	private final SessionFactory sessionFactory;

	private final ProductService productService;

	@Override
	public Page<TransactionEntity> getTransactions(String receiptNumber, int pageNo, int pageSize, String sortBy,
	                                               String order, LocalDate start, LocalDate end) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

		if (!receiptNumber.isEmpty() && start != null && end != null) {
			return transactionRepository.findByReceiptNumberContainingAndTransactionDateBetween(receiptNumber, Date.valueOf(start), Date.valueOf(end), pageable);
		} else if (receiptNumber.isEmpty() && start != null && end != null) {
			return transactionRepository.findByTransactionDateBetween(Date.valueOf(start), Date.valueOf(end), pageable);
		} else if (!receiptNumber.isEmpty() && start == null && end == null) {
			return transactionRepository.findByReceiptNumberContaining(receiptNumber, pageable);
		} else {
			return transactionRepository.findAll(pageable);
		}
	}

	@Override
	public TransactionEntity createTransaction(TransactionEntity transactionEntity) {

		double price = productService.getProductPrice(transactionEntity.getProductEntity().getId());
		transactionEntity.setDueMoney(price * transactionEntity.getAmount());

		return transactionRepository.save(transactionEntity);
	}

	@Override
	public TransactionEntity updateTransaction(TransactionEntity transactionEntity) {

		if (transactionRepository.existsById(transactionEntity.getId())) {

			double price = productService.getProductPrice(transactionEntity.getProductEntity().getId());
			transactionEntity.setDueMoney(price * transactionEntity.getAmount());
			return transactionRepository.save(transactionEntity);
		} else {
			throw new EntityNotFoundException("Transaction with ID " + transactionEntity.getId() + " not found");
		}
	}

	@Override
	public void deleteTransactionById(long id) {

		transactionRepository.deleteById(id);
	}

	@Override
	@SneakyThrows
	public CustomerReport getCustomerReport(long id, String receiptNumber, int pageNo, int pageSize,
	                                        String sortBy, String order, LocalDate start, LocalDate end) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);
		Page<TransactionEntity> page;

		if (receiptNumber.isEmpty()) {
			if (start == null || end == null) {
				page = transactionRepository.findByCustomerEntity_Id(id, pageable);
			} else {
				page = transactionRepository.findByCustomerEntity_IdAndTransactionDateBetween(id,
						Date.valueOf(start),
						Date.valueOf(end), pageable);
			}
		} else {
			if (start == null || end == null) {
				page = transactionRepository.findByCustomerEntity_IdAndReceiptNumberContaining(id, receiptNumber, pageable);
			} else {
				page = transactionRepository.findByCustomerEntity_IdAndReceiptNumberContainingAndTransactionDateBetween(id,
						receiptNumber,
						Date.valueOf(start),
						Date.valueOf(end), pageable);
			}
		}
		double totalDueMoney = -1;
		double totalPaidMoney = -1;
		if (page.hasContent()) {
			totalDueMoney = getColumnSum(id, "dueMoney", start, end);
			totalPaidMoney = getColumnSum(id, "paidMoney", start, end);

		}
		return new CustomerReport(page, totalDueMoney, totalPaidMoney);
	}

	@Transactional
	@SneakyThrows
	public Double getColumnSum(long id, String columnName, LocalDate start, LocalDate end) {

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
		if (start != null && end != null) {
			criteria.where(builder.and(
					criteria.getRestriction(),
					builder.between(root.get("transactionDate"), Date.valueOf(start), Date.valueOf(end))));
		}

		TypedQuery<Double> query = session.createQuery(criteria);
		Double result = query.getSingleResult();
		session.close();
		return result;
	}

}
