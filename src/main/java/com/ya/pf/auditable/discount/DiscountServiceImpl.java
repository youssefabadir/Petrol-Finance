package com.ya.pf.auditable.discount;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    @Override
    public DiscountEntity createDiscount(DiscountEntity customerDiscount) {

        if (customerDiscount.getId() != null) {
            customerDiscount.setId(null);
        }
        return discountRepository.save(customerDiscount);
    }

    @Override
    public DiscountEntity updateDiscount(DiscountEntity customerDiscount) {

        if (discountRepository.existsById(customerDiscount.getId())) {
            return discountRepository.save(customerDiscount);
        } else {
            throw new EntityNotFoundException("Discount with ID " + customerDiscount.getId() + " not found");
        }
    }

    @Override
    public void deleteDiscount(long id) {

        if (discountRepository.existsById(id)) {
            discountRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Discount with ID " + id + " not found");
        }
    }

	@Override
	public double getCustomerDiscountForProduct(long customerId, long productId) {

        DiscountEntity discountEntity = discountRepository.findByCustomerEntity_IdAndProductEntity_Id(customerId, productId);
        if (discountEntity != null) {
            return discountEntity.getDiscount();
        } else {
            throw new EntityNotFoundException("There is no discount for this customer id " + customerId + " for this product id " + productId);
        }
    }

}
