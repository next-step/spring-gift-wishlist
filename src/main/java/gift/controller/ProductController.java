package gift.controller;

import gift.dto.ErrorResponse;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    // 상품 추가 API
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestAttribute("userEmail") String userEmail,
                                        @Valid @RequestBody ProductRequest request,
                                        BindingResult bindingResult) {
        System.out.println("상품 추가 요청 사용자: " + userEmail);

        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMsg));
        }
        ProductResponse response = productService.addProduct(request);
        return ResponseEntity.created(URI.create("/api/products/" + response.id())).body(response);
    }

    // 전체 상품 조회 API
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllProducts(@RequestAttribute("userEmail") String userEmail) {
        System.out.println("전체 상품 조회 사용자: " + userEmail);
        List<ProductResponse> productResponses = productService.findAllProducts();
        return ResponseEntity.ok(productResponses);
    }

    // 특정 상품 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProduct(@RequestAttribute("userEmail") String userEmail,
                                                       @PathVariable Long id) {
        System.out.println("특정 상품 조회 사용자: " + userEmail);
        ProductResponse productResponse = productService.findProductById(id);
        return ResponseEntity.ok(productResponse);
    }

    // 상품 수정 API
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@RequestAttribute("userEmail") String userEmail,
                                           @PathVariable Long id,
                                           @Valid @RequestBody ProductRequest request,
                                           BindingResult bindingResult) {
        System.out.println("상품 수정 요청 사용자: " + userEmail);

        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMsg));
        }
        ProductResponse updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    // 상품 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@RequestAttribute("userEmail") String userEmail,
                                              @PathVariable Long id) {
        System.out.println("상품 삭제 요청 사용자: " + userEmail);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}