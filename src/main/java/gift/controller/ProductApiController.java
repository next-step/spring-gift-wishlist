package gift.controller;

import gift.common.exception.InvalidUserException;
import gift.domain.Product;
import gift.domain.Role;
import gift.dto.product.CreateProductRequest;
import gift.dto.product.ProductResponse;
import gift.dto.product.UpdateProductRequest;
import gift.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request, HttpServletRequest httpServletRequest) {
        validRoleAndName(httpServletRequest, request.name());
        Product product = productService.saveProduct(request);
        ProductResponse response = ProductResponse.from(product);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        List<ProductResponse> products = productService.getAllProducts().stream().map(ProductResponse::from).toList();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest request, HttpServletRequest httpServletRequest) {
        validRoleAndName(httpServletRequest, request.name());
        productService.updateProduct(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private void validRoleAndName(HttpServletRequest request, String name) {
        if (request.getAttribute("role") != Role.ADMIN) {
            if (name.contains("카카오")) {
                throw new InvalidUserException("이름에 [카카오]가 들어간 상품은 관리자만 생성 가능합니다.");
            }
        }
    }
}
