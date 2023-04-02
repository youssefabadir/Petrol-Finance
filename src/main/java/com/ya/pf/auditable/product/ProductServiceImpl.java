package com.ya.pf.auditable.product;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Override
	public Page<ProductEntity> getProducts(String name, int pageNo, int pageSize, String sortBy, String order) {

		Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

		if (name.trim().isEmpty()) {
			return productRepository.findAll(pageable);
		} else {
			return productRepository.findByNameContaining(name, pageable);
		}
	}

	@Override
	public ProductEntity createProduct(ProductEntity productEntity) {

		return productRepository.save(productEntity);
	}

	@Override
	public ProductEntity updateProduct(ProductEntity productEntity) {

		if (productRepository.existsById(productEntity.getId())) {
			return productRepository.save(productEntity);
		} else {
			throw new EntityNotFoundException("Product with ID " + productEntity.getId() + " not found");
		}
	}

	@Override
	public void deleteProduct(long id) {

		productRepository.deleteById(id);
	}

	@Override
	public double getProductPrice(long id) {

		ProductEntity product = productRepository.getReferenceById(id);
		return product.getPrice();
	}

	@Override
	public List<ProductEntity> searchProduct(String name) {

		return productRepository.findByNameContaining(name);
	}

}
