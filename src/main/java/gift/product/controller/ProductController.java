package gift.product.controller;

import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    // 의존성 주입
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody @Valid ProductRequestDto requestDto) {

        ProductResponseDto responseDto = productService.addProduct(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {

        ProductResponseDto responseDto = productService.getProductById(id);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto requestDto
    ) {

        ProductResponseDto updatedProduct = productService.updateProduct(id, requestDto);

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDto> deleteProduct(@PathVariable Long id) {

        ProductResponseDto responseDto = productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {

        List<ProductResponseDto> products = productService.getAllProducts();

        return ResponseEntity.ok(products);
    }
}
