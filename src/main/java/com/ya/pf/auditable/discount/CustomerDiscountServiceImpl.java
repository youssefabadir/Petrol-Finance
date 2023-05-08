package com.ya.pf.auditable.discount;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerDiscountServiceImpl implements CustomerDiscountService {

	private final CustomerDiscountRepository customerDiscountRepository;

	@Override
	public Page<CustomerDiscountEntity> getCustomersDiscount(int pageNo, int pageSize, String sortBy, String order) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);
		return customerDiscountRepository.findAll(pageable);
	}

	@Override
	public CustomerDiscountEntity createCustomerDiscount(CustomerDiscountEntity customerDiscount) {

		if (customerDiscount.getId() != null) {
			customerDiscount.setId(null);
		}
		return customerDiscountRepository.save(customerDiscount);
	}

	@Override
	public CustomerDiscountEntity updateCustomerDiscount(CustomerDiscountEntity customerDiscount) {

		if (customerDiscountRepository.existsById(customerDiscount.getId())) {
			return customerDiscountRepository.save(customerDiscount);
		} else {
			throw new EntityNotFoundException("Discount with ID " + customerDiscount.getId() + " not found");
		}
	}

	@Override
	public void deleteCustomerDiscount(long id) {

		if (customerDiscountRepository.existsById(id)) {
			customerDiscountRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("Discount with ID " + id + " not found");
		}
	}

	@Override
	public double getCustomerDiscountForProduct(long customerId, long productId) {

		CustomerDiscountEntity customerDiscountEntity = customerDiscountRepository.findByCustomerEntity_IdAndProductEntity_Id(customerId, productId);
		if (customerDiscountEntity != null) {
			return customerDiscountEntity.getDiscount();
		} else {
			throw new EntityNotFoundException("There is no discount for this customer id " + customerId + " for this product id " + productId);
		}
	}

}
