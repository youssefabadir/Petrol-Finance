package com.ya.pf.auditable.payment;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

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
	public PaymentEntity createPayment(PaymentEntity paymentEntity) {

		if (paymentEntity.getId() != null) {
			paymentEntity.setId(null);
		}

		boolean exists = paymentRepository.existsByNumberAndWayOfPaymentEntity_Id(paymentEntity.getNumber(),
				paymentEntity.getWayOfPaymentEntity().getId());
		if (exists) {
			throw new EntityExistsException("This receipt number exists for this way of payment");
		} else {
			return paymentRepository.save(paymentEntity);
		}
	}

	@Override
	public PaymentEntity updatePayment(PaymentEntity paymentEntity) {

		long id = paymentEntity.getId();
		if (paymentRepository.existsById(id)) {
			boolean uniquePayment = paymentRepository.checkUniquePayment(id, paymentEntity.getNumber(), paymentEntity.getWayOfPaymentEntity().getId());

			if (uniquePayment) {
				return paymentRepository.save(paymentEntity);
			} else {
				throw new EntityExistsException("This receipt number exists for this way of payment");
			}
		} else {
			throw new EntityNotFoundException("Payment with Id " + id + " not found");
		}
	}

	@Override
	public void deletePayment(long id) {

		if (paymentRepository.existsById(id)) {
			paymentRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("Payment with Id " + id + " not found");
		}
	}

}
