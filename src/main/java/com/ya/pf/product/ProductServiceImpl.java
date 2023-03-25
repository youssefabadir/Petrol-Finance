package com.ya.pf.product;

import com.ya.pf.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<ProductEntity> getProducts(String name, int pageNo, int pageSize, String sortBy, String order) {

        Pageable pageable = Helper.preparePageable(pageNo, pageSize, sortBy, order);

        if (name.isEmpty()) {
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

        return productRepository.save(productEntity);
    }

    @Override
    public void deleteProduct(long id) {

        productRepository.deleteById(id);
    }

    @Override
    public List<ProductEntity> searchProduct(String name) {

        return productRepository.findByNameContaining(name);
    }

    @Override
    public double getProductPrice(long id) {

        ProductEntity product = productRepository.getReferenceById(id);
        return product.getPrice();
    }

}
