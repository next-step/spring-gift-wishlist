package gift.controller;

import gift.model.CustomPage;
import gift.dto.ProductCreateRequest;
import gift.dto.ProductUpdateRequest;
import gift.entity.Product;
import gift.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<CustomPage<Product>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이여야 합니다.") Integer page,
            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "페이지 크기는 양수여야 합니다.") Integer size
    ) {
        return new ResponseEntity<>(productService.getBy(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable @Min(value = 0, message = "상품 ID는 0보다 커야합니다.") Long id
    ) {
        Product product = productService.getById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody ProductCreateRequest dto,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        Product product = productService.create(dto.toProduct());
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0보다 커야합니다.") Long id,
            @RequestBody ProductUpdateRequest dto) {
        if (id == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }
        Product updatedProduct = productService.update(dto.toEntity(id));
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0보다 커야합니다.") Long id,
            @Valid  @RequestBody ProductUpdateRequest dto,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
        ) {
        Product patchedProduct = productService.patch(dto.toEntity(id));
        return new ResponseEntity<>(patchedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0보다 커야합니다.") Long id
    ) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
