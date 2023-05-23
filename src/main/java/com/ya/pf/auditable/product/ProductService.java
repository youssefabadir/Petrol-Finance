package com.ya.pf.auditable.product;

import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Page<ProductEntity> getProducts(String name, int pageNo, int pageSize, String sortBy, String order);

    ProductEntity createProduct(ProductEntity productEntity);

    ProductEntity updateProduct(ProductEntity productEntity);

    void deleteProduct(long id);

    double getProductCustomerPrice(long id);

    List<ProductEntity> searchProduct(String name);

}
