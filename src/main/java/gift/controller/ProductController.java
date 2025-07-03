package gift.controller;

import gift.dto.*;
import gift.entity.*;

import gift.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//import java.math.BigDecimal;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        List<Product> allProducts = new ArrayList<>(repository.findAll());
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<Product> product = repository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody @valid ProductRequestDto request) {
        /*
        String error = validate(request);
        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        */


        Product product = new Product(
                null,
                request.getName(),
                request.getPrice(),
                request.getImgUrl()
        );
        Product saved = repository.create(product);

        URI location = URI.create("/api/products/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductRequestDto request) {
        Product existing = repository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        String error = validate(request);
        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }

        Product updated = new Product(
                id,
                request.getName(),
                request.getPrice(),
                request.getImgUrl()
        );
        repository.update(updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /*
    private String validate(ProductRequestDto request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return "상품 이름은 비어 있을 수 없습니다.";
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return "가격은 0 이상이어야 합니다.";
        }
        if (request.getImgUrl() == null || request.getImgUrl().isBlank()) {
            return "이미지 URL은 비어 있을 수 없습니다.";
        }
        return null;
    }
    */
}
