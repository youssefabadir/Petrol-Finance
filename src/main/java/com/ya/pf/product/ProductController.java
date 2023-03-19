package com.ya.pf.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<String> getProducts(@RequestParam(defaultValue = "") String name,
                                              @RequestParam(defaultValue = "0") int pageNo,
                                              @RequestParam(defaultValue = "10") int pageSize,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(defaultValue = "asc") String order) {

        Page<ProductEntity> productEntities = productService.getProducts(name, pageNo, pageSize, sortBy, order);

        if (productEntities.hasContent()) {
            try {
                return ResponseEntity.ok().body(new ObjectMapper().writeValueAsString(productEntities));
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product) {

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @PutMapping
    public ResponseEntity<ProductEntity> updateProduct(@RequestBody ProductEntity product) {

        return ResponseEntity.ok().body(productService.updateProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {

        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
