package com.ya.pf.auditable.owner_payment;

import com.ya.pf.auditable.customer_payment.CustomerPaymentService;
import com.ya.pf.auditable.ownerTransaction.entity.OwnerTransactionService;
import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class OwnerPaymentServiceImpl implements OwnerPaymentService {

	private final OwnerPaymentRepository ownerPaymentRepository;

	private final OwnerTransactionService ownerTransactionService;

	@Lazy
	private final CustomerPaymentService customerPaymentService;

	@Override
	public Page<OwnerPaymentEntity> getOwnerPayments(String number, int pageNo, int pageSize, String sortBy, String order) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

		if (number.trim().isEmpty()) {
			return ownerPaymentRepository.findAll(pageable);
		} else {
			return ownerPaymentRepository.findByNumberContaining(number, pageable);
		}
	}

	@Override
	@Transactional
	public OwnerPaymentEntity createOwnerPayment(OwnerPaymentEntity ownerPaymentEntity) {

		if (ownerPaymentEntity.getId() != null) {
			ownerPaymentEntity.setId(null);
		}

		boolean exists = ownerPaymentRepository.existsByNumberAndPaymentMethodEntity_Id(ownerPaymentEntity.getNumber(), ownerPaymentEntity.getPaymentMethodEntity().getId());

		if (exists) {
			throw new EntityExistsException("This payment number exists for this way of payment");
		} else {
			ownerPaymentEntity.setAmount(Math.abs(ownerPaymentEntity.getAmount()));
			OwnerPaymentEntity ownerPayment = ownerPaymentRepository.save(ownerPaymentEntity);

			ownerTransactionService.createOwnerTransaction(ownerPayment.getSupplierEntity().getId(), ownerPayment.getAmount(),
					ownerPayment.getId(), null, ownerPayment.getDate());
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteOwnerPayment(long id) {

		if (ownerPaymentRepository.existsById(id)) {
			OwnerPaymentEntity ownerPaymentEntity = ownerPaymentRepository.getReferenceById(id);
			long paymentId = ownerPaymentEntity.getId();
			double paymentAmount = ownerPaymentEntity.getAmount();
			String paymentNumber = ownerPaymentEntity.getNumber();

			ownerPaymentRepository.deleteById(id);

			ownerTransactionService.deleteOwnerTransactionByPaymentId(paymentId, paymentAmount);

			if (ownerPaymentEntity.isTransferred()) {
				customerPaymentService.deleteTransferredCustomerPayment(paymentNumber, ownerPaymentEntity.getPaymentMethodEntity().getId());
			}

		} else {
			throw new EntityNotFoundException("This owner payment id " + id + " doesn't exists");
		}
	}

	@Override
	@Transactional
	public void deleteTransferredOwnerPayment(String number, long paymentMethodId) {

		OwnerPaymentEntity ownerPaymentEntity = ownerPaymentRepository.getByNumberAndPaymentMethodEntity_Id(number, paymentMethodId);

		if (ownerPaymentEntity != null) {
			if (ownerPaymentEntity.isTransferred()) {
				long paymentId = ownerPaymentEntity.getId();
				double paymentAmount = ownerPaymentEntity.getAmount();

				ownerPaymentRepository.deleteById(paymentId);

				ownerTransactionService.deleteOwnerTransactionByPaymentId(paymentId, paymentAmount);
			}
		} else {
			throw new EntityNotFoundException("This owner payment number doesn't exists");
		}
	}

}
