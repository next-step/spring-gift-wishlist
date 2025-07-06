package gift.controller.api;

import gift.common.model.CustomAuth;
import gift.dto.product.ProductResponse;
import gift.common.model.CustomPage;
import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductUpdateRequest;
import gift.entity.Product;
import gift.service.product.ProductService;
import gift.common.validation.group.AuthenticationGroups;
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

    private <T> void handleGroupValidation (CustomAuth auth, T dto) {
        if (auth == null) {
            log.error("인증 정보가 없습니다. 요청 헤더에 Authorization 헤더를 추가해야 합니다.");
            throw new IllegalArgumentException("인증 정보가 없습니다. 요청 헤더에 Authorization 헤더를 추가해야 합니다.");
        }
        if (dto != null) {
            Class<?> group = switch (auth.role()) {
                case ROLE_ADMIN -> AuthenticationGroups.AdminGroup.class;
                case ROLE_MD -> AuthenticationGroups.MdGroup.class;
                case ROLE_USER -> AuthenticationGroups.UserGroup.class;
            };

            Set<ConstraintViolation<T>> violations = validator.validate(dto, group);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException("유효하지 않은 요청입니다.", violations);
            }
        }
    }

    @GetMapping
    public ResponseEntity<CustomPage<ProductResponse>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이여야 합니다.") Integer page,
            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "페이지 크기는 양수여야 합니다.") Integer size,
            @RequestAttribute("auth") CustomAuth auth
            ) {
        var productPage = CustomPage.convert(productService.getBy(page, size), ProductResponse::from);
        return new ResponseEntity<>(productPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        Product product = productService.getById(id);
        return new ResponseEntity<>(ProductResponse.from(product), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest dto,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        handleGroupValidation(auth, dto);
        Product product = productService.create(dto.toProduct());
        log.info("상품 생성 성공: {}", product);
        return new ResponseEntity<>(ProductResponse.from(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            @Valid @RequestBody ProductUpdateRequest dto,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        handleGroupValidation(auth, dto);
        Product updatedProduct = productService.update(dto.toEntity(id));
        log.info("상품 업데이트 성공: {}", updatedProduct);
        return new ResponseEntity<>(ProductResponse.from(updatedProduct), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> patchProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            @Valid @RequestBody ProductUpdateRequest dto,
            @RequestAttribute("auth") CustomAuth auth
        ) {
        handleGroupValidation(auth, dto);
        Product patchedProduct = productService.patch(dto.toEntity(id));
        log.info("상품 패치 성공: {}", patchedProduct);
        return new ResponseEntity<>(ProductResponse.from(patchedProduct), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            @RequestAttribute("auth") CustomAuth auth
    ) {
        handleGroupValidation(auth, null);
        productService.deleteById(id);
        log.info("상품 삭제 성공: ID={}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
