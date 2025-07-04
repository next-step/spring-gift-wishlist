package gift.controller.api;

import gift.model.CustomPage;
import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductUpdateRequest;
import gift.entity.Product;
import gift.service.product.ProductService;
import gift.validation.group.AuthenticationGroups;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@Controller
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private final Validator validator;

    public ProductController(ProductService productService, Validator validator) {
        this.productService = productService;
        this.validator = validator;
    }

    private <T> void handleGroupValidation (String userRole, T dto) {
        Class<?> group = AuthenticationGroups.UserGroup.class;
        if (userRole != null) {
            group = switch (userRole.toUpperCase()) {
                case "ROLE_ADMIN" -> AuthenticationGroups.AdminGroup.class;
                case "ROLE_MD" -> AuthenticationGroups.MdGroup.class;
                case "ROLE_USER" -> AuthenticationGroups.UserGroup.class;
                default -> {
                    log.error("알 수 없는 사용자 역할: {}", userRole);
                    throw new IllegalArgumentException("알 수 없는 사용자 역할입니다. : " + userRole);
                }
            };
        }
        Set<ConstraintViolation<T>> violations = validator.validate(dto, group);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("유효하지 않은 요청입니다.", violations);
        }
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
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id
    ) {
        Product product = productService.getById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody ProductCreateRequest dto,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        handleGroupValidation(userRole, dto);
        Product product = productService.create(dto.toProduct());
        log.info("상품 생성 성공: {}", product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            @Valid @RequestBody ProductUpdateRequest dto,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        handleGroupValidation(userRole, dto);
        Product updatedProduct = productService.update(dto.toEntity(id));
        log.info("상품 업데이트 성공: {}", updatedProduct);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            @Valid @RequestBody ProductUpdateRequest dto,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
        ) {
        handleGroupValidation(userRole, dto);
        Product patchedProduct = productService.patch(dto.toEntity(id));
        log.info("상품 패치 성공: {}", patchedProduct);
        return new ResponseEntity<>(patchedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id
    ) {
        productService.deleteById(id);
        log.info("상품 삭제 성공: ID={}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
