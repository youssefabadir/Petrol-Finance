package com.ya.pf.auditable.payment;

import com.ya.pf.auditable.transaction.entity.TransactionService;
import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	private final TransactionService transactionService;

	@Override
	public Page<PaymentEntity> getPayments(String receiptNumber, int pageNo, int pageSize, String sortBy, String order) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

		if (receiptNumber.trim().isEmpty()) {
			return paymentRepository.findAll(pageable);
		} else {
			return paymentRepository.findByNumberContaining(receiptNumber, pageable);
		}
	}

	@Override
	@Transactional
	public PaymentEntity createPayment(PaymentEntity paymentEntity) {

		if (paymentEntity.getId() != null) {
			paymentEntity.setId(null);
		}

		boolean exists = paymentRepository.existsByNumberAndPaymentMethodEntity_Id(paymentEntity.getNumber(),
				paymentEntity.getPaymentMethodEntity().getId());
		if (exists) {
			throw new EntityExistsException("This receipt number exists for this way of payment");
		} else {
			PaymentEntity payment = paymentRepository.save(paymentEntity);
			transactionService.createTransaction(payment.getCustomerEntity().getId(), payment.getSupplierEntity().getId(),
					payment.getAmount(), payment.getId(), null, payment.getDate());
			return payment;
		}
	}

	@Override
	@Transactional
	public void deletePayment(long id) {

		if (paymentRepository.existsById(id)) {
			PaymentEntity payment = paymentRepository.getReferenceById(id);

			paymentRepository.deleteById(id);
			transactionService.deleteTransactionByPaymentId(id, payment.getAmount());
		} else {
			throw new EntityNotFoundException("Payment with Id " + id + " not found");
		}
	}

}
