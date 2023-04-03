package com.ya.pf.auditable.bill;

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
public class BillServiceImpl implements BillService {

	private final BillRepository billRepository;

	private final SessionFactory sessionFactory;

	private final ProductService productService;

	@Override
	public Page<BillEntity> getBills(String receiptNumber, int pageNo, int pageSize, String sortBy,
	                                 String order, LocalDate start, LocalDate end) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

		if (!receiptNumber.isEmpty() && start != null && end != null) {
			return billRepository.findByNumberContainingAndDateBetween(receiptNumber, Date.valueOf(start), Date.valueOf(end), pageable);
		} else if (receiptNumber.isEmpty() && start != null && end != null) {
			return billRepository.findByDateBetween(Date.valueOf(start), Date.valueOf(end), pageable);
		} else if (!receiptNumber.isEmpty() && start == null && end == null) {
			return billRepository.findByNumberContaining(receiptNumber, pageable);
		} else {
			return billRepository.findAll(pageable);
		}
	}

	@Override
	public BillEntity createBill(BillEntity billEntity) {

		if (billEntity.getId() != null) {
			billEntity.setId(null);
		}

		double price = productService.getProductPrice(billEntity.getProductEntity().getId());
		billEntity.setAmount(price * billEntity.getAmount());

		return billRepository.save(billEntity);
	}

	@Override
	public BillEntity updateBill(BillEntity billEntity) {

		if (billRepository.existsById(billEntity.getId())) {

			double price = productService.getProductPrice(billEntity.getProductEntity().getId());
			billEntity.setAmount(price * billEntity.getAmount());
			return billRepository.save(billEntity);
		} else {
			throw new EntityNotFoundException("Bill with ID " + billEntity.getId() + " not found");
		}
	}

	@Override
	public void deleteBill(long id) {

		if (billRepository.existsById(id)) {

			billRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("Bill with ID " + id + " not found");
		}
	}

	@Override
	@SneakyThrows
	public CustomerReport getCustomerReport(long id, String receiptNumber, int pageNo, int pageSize,
	                                        String sortBy, String order, LocalDate start, LocalDate end) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);
		Page<BillEntity> page;

		if (receiptNumber.isEmpty()) {
			if (start == null || end == null) {
				page = billRepository.findByCustomerEntity_Id(id, pageable);
			} else {
				page = billRepository.findByCustomerEntity_IdAndDateBetween(id,
						Date.valueOf(start),
						Date.valueOf(end), pageable);
			}
		} else {
			if (start == null || end == null) {
				page = billRepository.findByCustomerEntity_IdAndNumberContaining(id, receiptNumber, pageable);
			} else {
				page = billRepository.findByCustomerEntity_IdAndNumberContainingAndDateBetween(id,
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
		Root<BillEntity> root = criteria.from(BillEntity.class);

		criteria.select(builder.sum(root.get(columnName)));
		criteria.where(builder.equal(root.get("customerEntity"), id));
		if (start != null && end != null) {
			criteria.where(builder.and(
					criteria.getRestriction(),
					builder.between(root.get("billDate"), Date.valueOf(start), Date.valueOf(end))));
		}

		TypedQuery<Double> query = session.createQuery(criteria);
		Double result = query.getSingleResult();
		session.close();
		return result;
	}

}
