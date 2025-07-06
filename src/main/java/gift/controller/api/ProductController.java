package gift.controller.api;

import gift.common.aop.annotation.PreAuthorize;
import gift.common.model.CustomAuth;
import gift.dto.product.ProductDefaultResponse;
import gift.common.model.CustomPage;
import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductUpdateRequest;
import gift.entity.Product;
import gift.entity.UserRole;
import gift.service.product.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<CustomPage<ProductDefaultResponse>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이여야 합니다.") Integer page,
            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "페이지 크기는 양수여야 합니다.") Integer size
            ) {
        var productPage = CustomPage.convert(productService.getBy(page, size), ProductDefaultResponse::from);
        return new ResponseEntity<>(productPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDefaultResponse> getProductById(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id
    ) {
        Product product = productService.getById(id);
        return new ResponseEntity<>(ProductDefaultResponse.from(product), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<ProductDefaultResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest dto
    ) {
        Product product = productService.create(dto.toProduct());
        log.info("상품 생성 성공: {}", product);
        return new ResponseEntity<>(ProductDefaultResponse.from(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<ProductDefaultResponse> updateProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            @Valid @RequestBody ProductUpdateRequest dto,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        Product updatedProduct = productService.update(dto.toEntity(id), auth);
        log.info("상품 업데이트 성공: {}", updatedProduct);
        return new ResponseEntity<>(ProductDefaultResponse.from(updatedProduct), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<ProductDefaultResponse> patchProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            @Valid @RequestBody ProductUpdateRequest dto,
            @RequestAttribute("auth") CustomAuth auth
        ) {
        Product patchedProduct = productService.patch(dto.toEntity(id), auth);
        log.info("상품 패치 성공: {}", patchedProduct);
        return new ResponseEntity<>(ProductDefaultResponse.from(patchedProduct), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<Void> deleteProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        productService.deleteById(id, auth);
        log.info("상품 삭제 성공: ID={}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
