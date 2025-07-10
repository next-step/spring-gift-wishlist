package gift.controller;

import gift.domain.Product;
import gift.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/products")

public class ProductController {
    private final ProductRepository products;

    public ProductController(ProductRepository products) { this.products = products; }

    @GetMapping
    public List<Product> getProducts() {
        return new ArrayList<>(products.getProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProducts(@PathVariable Long id) {
        Optional<Product> storedProduct = products.findById(id);
        if (storedProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(storedProduct);
    }

    @PostMapping
    public Product addProducts(@RequestBody Product product) {
        return products.save(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProducts(@PathVariable Long id, @RequestBody Product update) {
        if (products.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        update.setId(id);
        products.update(id, update);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        if (products.findById(id).isPresent()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
