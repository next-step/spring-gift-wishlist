package gift.controller;


import gift.dto.request.ProductRequest;
import gift.dto.response.ProductResponse;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> register(@RequestBody @Valid ProductRequest request) {
        ProductResponse response = productService.register(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId, @RequestBody  @Valid ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(productId, request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<ProductResponse> searchByName(@RequestParam String name) {
        return productService.searchByName(name);
    }
}