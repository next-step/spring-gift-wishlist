package gift.controller;

import gift.common.dto.request.CreateProductDto;
import gift.common.dto.request.UpdateProductDto;
import gift.common.dto.response.MessageResponseDto;
import gift.common.dto.response.ProductDto;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<MessageResponseDto<ProductDto>> createProduct(@Valid @RequestBody CreateProductDto body) {
        var response = productService.createProduct(body);
        if (response.success()) {
            URI location = URI.create("/api/products/" + response.data().id());
            return ResponseEntity.created(location).body(response);
        }
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        ProductDto response = productService.getProduct(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        List<ProductDto> response = productService.getAllProduct();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDto<ProductDto>> updateProduct(@PathVariable Long id,
                                                    @RequestBody UpdateProductDto body) {
        var response = productService.updateProduct(id, body);
        if (response.success()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.accepted().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
