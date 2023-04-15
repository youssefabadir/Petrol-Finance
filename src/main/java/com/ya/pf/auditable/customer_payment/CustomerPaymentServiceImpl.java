package com.ya.pf.auditable.customer_payment;

import com.ya.pf.auditable.customer_transaction.entity.CustomerTransactionService;
import com.ya.pf.auditable.owner_payment.OwnerPaymentEntity;
import com.ya.pf.auditable.owner_payment.OwnerPaymentService;
import com.ya.pf.auditable.supplier.SupplierEntity;
import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerPaymentServiceImpl implements CustomerPaymentService {

	private final CustomerPaymentRepository customerPaymentRepository;

	private final CustomerTransactionService customerTransactionService;

	private final OwnerPaymentService ownerPaymentService;

	@Override
	public Page<CustomerPaymentEntity> getCustomerPayments(String number, int pageNo, int pageSize, String sortBy, String order) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

		if (number.trim().isEmpty()) {
			return customerPaymentRepository.findAll(pageable);
		} else {
			return customerPaymentRepository.findByNumberContaining(number, pageable);
		}
	}

	@Override
	@Transactional
	public CustomerPaymentEntity createCustomerPayment(CustomerPaymentEntity customerPaymentEntity, boolean transferred, long supplierId) {

		if (customerPaymentEntity.getId() != null) {
			customerPaymentEntity.setId(null);
		}

		long paymentMethodId = customerPaymentEntity.getPaymentMethodEntity().getId();
		String paymentNumber = customerPaymentEntity.getNumber();

		if (paymentMethodId != 1) {
			boolean exists = customerPaymentRepository.existsByNumberAndPaymentMethodEntity_Id(paymentNumber, paymentMethodId);
			if (exists) {
				throw new EntityExistsException("This payment number exists for this way of payment");
			}
		}

		customerPaymentEntity.setAmount(Math.abs(customerPaymentEntity.getAmount()));
		CustomerPaymentEntity payment = customerPaymentRepository.save(customerPaymentEntity);
		long paymentId = payment.getId();
		double paymentAmount = payment.getAmount();
		Date paymentDate = payment.getDate();

		customerTransactionService.createCustomerTransaction(payment.getCustomerEntity().getId(), paymentAmount, paymentId, null, paymentDate);

		if (transferred) {
			OwnerPaymentEntity ownerPaymentEntity = new OwnerPaymentEntity();
			ownerPaymentEntity.setAmount(paymentAmount);
			ownerPaymentEntity.setPaymentMethodEntity(payment.getPaymentMethodEntity());
			ownerPaymentEntity.setNumber(paymentNumber);
			ownerPaymentEntity.setSupplierEntity(new SupplierEntity().setId(supplierId));
			ownerPaymentEntity.setTransferred(true);
			ownerPaymentEntity.setDate(paymentDate);
			ownerPaymentService.createOwnerPayment(ownerPaymentEntity);
		}

		return payment;
	}

	@Override
	@Transactional
	public void deleteCustomerPayment(long paymentId) {

		if (customerPaymentRepository.existsById(paymentId)) {
			CustomerPaymentEntity payment = customerPaymentRepository.getReferenceById(paymentId);
			double amount = payment.getAmount();
			boolean transferred = payment.isTransferred();
			String paymentNumber = payment.getNumber();

			customerPaymentRepository.deleteById(paymentId);

			customerTransactionService.deleteCustomerTransactionByPaymentId(paymentId, amount);

			if (transferred) {
				ownerPaymentService.deleteTransferredOwnerPayment(paymentNumber, payment.getPaymentMethodEntity().getId());
			}
		} else {
			throw new EntityNotFoundException("Payment with Id " + paymentId + " not found");
		}
	}

	@Override
	@Transactional
	public void deleteTransferredCustomerPayment(String number, long paymentMethodId) {

		CustomerPaymentEntity customerPaymentEntity = customerPaymentRepository.getByNumberAndPaymentMethodEntity_Id(number, paymentMethodId);

		if (customerPaymentEntity != null) {
			if (customerPaymentEntity.isTransferred()) {
				long paymentId = customerPaymentEntity.getId();
				double paymentAmount = customerPaymentEntity.getAmount();

				customerPaymentRepository.deleteById(paymentId);

				customerTransactionService.deleteCustomerTransactionByPaymentId(paymentId, paymentAmount);
			}
		} else {
			throw new EntityNotFoundException("This customer payment number doesn't exists");
		}
	}

}
