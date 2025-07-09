package gift.controller;

import gift.component.JwtUtil;
import gift.dto.CreateProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateProductRequestDto;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;
    private final JwtUtil jwtUtil;

    public ProductController(ProductService productService, JwtUtil jwtUtil) {
        this.productService = productService;
        this.jwtUtil = jwtUtil;
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findAllProducts() {
        List<ProductResponseDto> list = productService.findAllProducts();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 상품 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findProductById(@PathVariable Long id) {
        ProductResponseDto productResponseDto = productService.findProductById(id);

        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    // 상품 추가
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody CreateProductRequestDto requestDto) {
        jwtUtil.validateAuthorizationHeader(authHeader, "products-api");
        ProductResponseDto productResponseDto = productService.createProduct(requestDto);

        return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {
        jwtUtil.validateAuthorizationHeader(authHeader, "products-api");
        productService.deleteProduct(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 상품 전체 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequestDto requestDto
    ) {
        jwtUtil.validateAuthorizationHeader(authHeader, "products-api");
        productService.updateProduct(requestDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
