package com.ya.pf.auditable.product;

import com.ya.pf.auditable.product.dto.ProductDTO;
import com.ya.pf.auditable.product.dto.ProductDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/product")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {

    private final ProductService productService;

    private final ProductDTOMapper productDTOMapper;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProducts(@RequestParam(defaultValue = "") String name,
                                                        @RequestParam(defaultValue = "0") int pageNo,
                                                        @RequestParam(defaultValue = "10") int pageSize,
                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String order) {

        try {
            Page<ProductEntity> productEntities = productService.getProducts(name, pageNo, pageSize, sortBy, order);
            Page<ProductDTO> productDTOS = productEntities.map(productDTOMapper);
            return ResponseEntity.ok(productDTOS);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductEntity product) {

        try {
            ProductEntity productEntity = productService.createProduct(product);
            ProductDTO productDTO = productDTOMapper.apply(productEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductEntity product) {

        try {
            ProductEntity productEntity = productService.updateProduct(product);
            ProductDTO productDTO = productDTOMapper.apply(productEntity);
            return ResponseEntity.ok(productDTO);
        } catch (EntityNotFoundException e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {

        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchCustomer(@RequestParam(defaultValue = "") String name) {

        if (name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            List<ProductEntity> productEntities = productService.searchProduct(name);
            List<ProductDTO> productDTOS = productEntities.stream().map(productDTOMapper).toList();
            return ResponseEntity.ok(productDTOS);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
