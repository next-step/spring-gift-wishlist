package gift.domain.product.controller;

import gift.application.product.ProductApplicationService;
import gift.common.pagination.Page;
import gift.common.pagination.Pageable;
import gift.domain.product.dto.ProductRequest;
import gift.domain.product.dto.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductApplicationService productApplicationService;

    public ProductController(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productApplicationService.addProduct(productRequest);
        return ResponseEntity.created(URI.create("/api/products/" + productResponse.id())).build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        Pageable pageable = new Pageable(page, size);
        Page<ProductResponse> productResponses = productApplicationService.getAllProducts(pageable);
        if (productResponses.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id) {
        ProductResponse productResponse = productApplicationService.getProduct(id);
        return ResponseEntity.ok(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id,
                                              @Valid @RequestBody ProductRequest productRequest) {
        productApplicationService.updateProduct(id, productRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productApplicationService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}