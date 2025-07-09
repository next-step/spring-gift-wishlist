package gift.product.controller;

import gift.product.dto.request.ProductSaveRequest;
import gift.product.dto.request.ProductUpdateRequest;
import gift.product.dto.response.ProductResponse;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
        @Valid
        @RequestBody
        ProductSaveRequest productSaveRequest
    ) {
        return ResponseEntity.ok(productService.save(productSaveRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable("id")
        Long id,

        @Valid
        @RequestBody
        ProductUpdateRequest productUpdateRequest
    ) {
        return ResponseEntity.ok(productService.update(id, productUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.ok("상품 삭제가 완료되었습니다.");
    }
}
