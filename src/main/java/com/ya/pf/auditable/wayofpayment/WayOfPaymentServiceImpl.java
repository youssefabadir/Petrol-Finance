package com.ya.pf.auditable.wayofpayment;

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
public class WayOfPaymentServiceImpl implements WayOfPaymentService {

	private final WayOfPaymentRepository wayOfPaymentRepository;

	@Override
	public Page<WayOfPaymentEntity> getWayOfPayments(String name, int pageNo, int pageSize, String sortBy, String order) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

		if (name.trim().isEmpty()) {
			return wayOfPaymentRepository.findAll(pageable);
		} else {
			return wayOfPaymentRepository.findByNameContaining(name, pageable);
		}
	}

	@Override
	public WayOfPaymentEntity createWayOfPayment(WayOfPaymentEntity wayOfPaymentEntity) {

		if (wayOfPaymentRepository.existsByName(wayOfPaymentEntity.getName())) {
			throw new EntityExistsException("Way of payment with this name already exists");
		}

		if (wayOfPaymentEntity.getId() != null) {
			wayOfPaymentEntity.setId(null);
		}

		return wayOfPaymentRepository.save(wayOfPaymentEntity);
	}

	@Override
	public WayOfPaymentEntity updateWayOfPayment(WayOfPaymentEntity wayOfPaymentEntity) {

		long id = wayOfPaymentEntity.getId();
		if (wayOfPaymentRepository.existsById(id)) {
			boolean uniqueWayOfPayment = wayOfPaymentRepository.checkUniquePayment(id, wayOfPaymentEntity.getName());

			if (uniqueWayOfPayment) {
				wayOfPaymentRepository.save(wayOfPaymentEntity);
			} else {
				throw new EntityExistsException("Way of payment with this name already exists");
			}
		} else {
			throw new EntityNotFoundException("Way of payment with Id " + id + " not found");
		}
		return null;
	}

	@Override
	public void deleteWayOfPayment(long id) {

		if (wayOfPaymentRepository.existsById(id)) {
			wayOfPaymentRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("Way of payment with Id " + id + " not found");
		}
	}

}
