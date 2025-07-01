package gift.controller;

import gift.model.CustomPage;
import gift.dto.ProductCreateRequest;
import gift.dto.ProductUpdateRequest;
import gift.entity.Product;
import gift.service.ProductService;
import gift.validation.group.ValidationGroups;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@Controller
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final Validator validator;

    public ProductController(ProductService productService, Validator validator) {
        this.productService = productService;
        this.validator = validator;
    }

    private <T> void handleGroupValidation (String userRole, T dto) {
        Class<?> group = ValidationGroups.UserGroup.class;
        if (userRole != null && userRole.equalsIgnoreCase("role_md")) {
            group = ValidationGroups.MDGroup.class;
        }
        Set<ConstraintViolation<T>> violations = validator.validate(dto, group);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
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
        handleGroupValidation(userRole, dto);
        Product product = productService.create(dto.toProduct());
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0보다 커야합니다.") Long id,
            @Valid @RequestBody ProductUpdateRequest dto,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        handleGroupValidation(userRole, dto);
        Product updatedProduct = productService.update(dto.toEntity(id));
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(
            @PathVariable @Min(value = 0, message = "상품 ID는 0보다 커야합니다.") Long id,
            @Valid @RequestBody ProductUpdateRequest dto,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
        ) {
        handleGroupValidation(userRole, dto);
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
