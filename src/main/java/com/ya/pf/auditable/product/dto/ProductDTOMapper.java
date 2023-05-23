package com.ya.pf.auditable.product.dto;

import com.ya.pf.auditable.product.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductDTOMapper implements Function<ProductEntity, ProductDTO> {

    @Override
    public ProductDTO apply(ProductEntity productEntity) {

        return new ProductDTO(productEntity.getId(), productEntity.getName(), productEntity.getCustomerPrice(), productEntity.getSupplierPrice());
    }

}
