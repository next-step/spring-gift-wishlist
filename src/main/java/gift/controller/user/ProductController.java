package gift.controller.user;

import gift.dto.product.ProductRequest;
import gift.dto.product.ProductResponse;
import gift.entity.product.Product;
import gift.exception.ProductNotFoundExection;
import gift.service.product.ProductService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService svc) {
        this.productService = svc;
    }

    /**
     * Request에서 인증된 사용자 role을 추출합니다.
     */
    public static String extractRole(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("authClaims");
        return claims.get("role", String.class);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(HttpServletRequest request) {
        String role = extractRole(request);
        List<ProductResponse> list = productService.getAllProducts(role).stream()
                .map(Product::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(
            HttpServletRequest request,
            @PathVariable Long id) {
        Product p = productService.getProductById(id, extractRole(request))
                .orElseThrow(() -> new ProductNotFoundExection(id));
        return ResponseEntity.ok(p.toResponse());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            HttpServletRequest request,
            @Valid @RequestBody ProductRequest dto) {
        Product saved = productService.createProduct(
                dto.name(), dto.price(), dto.imageUrl(), extractRole(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.toResponse());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            HttpServletRequest request,
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest dto) {
        Product updated = productService.updateProduct(
                id, dto.name(), dto.price(), dto.imageUrl(), extractRole(request));
        return ResponseEntity.ok(updated.toResponse());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            HttpServletRequest request,
            @PathVariable Long id) {
        productService.deleteProduct(id, extractRole(request));
        return ResponseEntity.noContent().build();
    }
}
