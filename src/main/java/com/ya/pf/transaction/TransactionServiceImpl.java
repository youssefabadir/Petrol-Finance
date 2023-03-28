package com.ya.pf.transaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ya.pf.product.ProductService;
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

		double price = productService.getProductPrice(transactionEntity.getProductEntity().getId());
		transactionEntity.setDueMoney(price * transactionEntity.getAmount());

		return transactionRepository.save(transactionEntity);
	}

	@Override
	public void deleteTransactionById(long id) {

		transactionRepository.deleteById(id);
	}

	@Override
	@SneakyThrows
	public String getCustomerReport(long id, String receiptNumber, int pageNo, int pageSize,
	                                String sortBy, String order, LocalDate start, LocalDate end) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize);
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

		String result = null;
		if (page.hasContent()) {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(page));

			double totalDueMoney = getColumnSum(id, "dueMoney", start, end);
			double totalPaidMoney = getColumnSum(id, "paidMoney", start, end);

			((ObjectNode) jsonNode).put("totalDueMoney", totalDueMoney);
			((ObjectNode) jsonNode).put("totalPaidMoney", totalPaidMoney);
			result = objectMapper.writeValueAsString(jsonNode);
		}
		return result;
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
