package com.ya.pf.auditable.product;

import com.ya.pf.auditable.product.dto.ProductDTO;
import com.ya.pf.auditable.product.dto.ProductDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductDTOMapper productDTOMapper;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProducts(@RequestParam(defaultValue = "") String name,
                                                        @RequestParam(defaultValue = "0") int pageNo,
                                                        @RequestParam(defaultValue = "10") int pageSize,
                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String order) {
        Page<ProductEntity> productEntities = productService.getProducts(name, pageNo, pageSize, sortBy, order);
        Page<ProductDTO> productDTOS = productEntities.map(productDTOMapper);
        return ResponseEntity.ok(productDTOS);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductEntity product) {
        ProductEntity productEntity = productService.createProduct(product);
        ProductDTO productDTO = productDTOMapper.apply(productEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @PutMapping
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductEntity product) {
        ProductEntity productEntity = productService.updateProduct(product);
        ProductDTO productDTO = productDTOMapper.apply(productEntity);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchCustomer(@RequestParam(defaultValue = "") String name) {

        if (name.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<ProductEntity> productEntities = productService.searchProduct(name);
        List<ProductDTO> productDTOS = productEntities.stream().map(productDTOMapper).toList();
        return ResponseEntity.ok(productDTOS);
    }

}
