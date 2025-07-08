package gift.controller;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.entity.product.Product;
import gift.exception.ProductNotFoundExection;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService svc) {
        this.productService = svc;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<ProductResponse> list = productService.getAllProducts().stream()
                .filter(p -> !p.hidden())
                .map(Product::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        Product p = findUnHiddenProduct(id);
        return ResponseEntity.ok(p.toResponse());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody ProductRequest req) {
        Product saved = productService.createProduct(req.name(), req.price(), req.imageUrl());
        return ResponseEntity.status(201).body(saved.toResponse());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest req) {
        Product p = findUnHiddenProduct(id);
        Product updated = productService.updateProduct(p.id().id(), req.name(), req.price(),
                req.imageUrl());
        return ResponseEntity.ok(updated.toResponse());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Product p = findUnHiddenProduct(id);
        productService.deleteProduct(p.id().id());
        return ResponseEntity.noContent().build();
    }

    private Product findUnHiddenProduct(Long id) {
        Product p = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundExection(id));
        if (p.hidden()) {
            throw new ProductNotFoundExection(id);
        }
        return p;
    }
}
