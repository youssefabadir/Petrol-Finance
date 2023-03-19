package com.ya.pf.product;

import org.springframework.data.domain.Page;

public interface ProductService {

    Page<ProductEntity> getProducts(String name, int pageNo, int pageSize, String sortBy, String order);

    ProductEntity createProduct(ProductEntity productEntity);

    ProductEntity updateProduct(ProductEntity productEntity);

    void deleteProduct(Long id);

}
