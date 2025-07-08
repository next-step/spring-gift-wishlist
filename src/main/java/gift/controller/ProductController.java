package gift.controller;

import gift.dto.CreateProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.service.ProductService;
import gift.service.TokenService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final TokenService tokenService;

    public ProductController(ProductService productService, TokenService tokenService) {
        this.productService = productService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody CreateProductRequestDto requestDto,
            @RequestHeader(value = "Authorization") String token) {
        Optional<Member> optionalMember = tokenService.isValidateToken(token);
        if (optionalMember.isEmpty()) {
            throw new CustomException(ErrorCode.NotLogin);
        }
        if (requestDto.name().contains("카카오") && !optionalMember.get().isAdmin()) {
            throw new CustomException(ErrorCode.NamingForbidden);
        }
        return new ResponseEntity<>(productService.createProduct(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findAllProducts() {
        return new ResponseEntity<>(productService.findAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findProductById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.findProductById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProductById(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequestDto requestDto,
            @RequestHeader(value = "Authorization") String token) {
        Optional<Member> optionalMember = tokenService.isValidateToken(token);
        if (optionalMember.isEmpty()) {
            throw new CustomException(ErrorCode.NotLogin);
        }
        if (requestDto.name().contains("카카오") && !optionalMember.get().isAdmin()) {
            throw new CustomException(ErrorCode.NamingForbidden);
        }

        return new ResponseEntity<>(productService.updateProductById(id, requestDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization") String token) {
        Optional<Member> optionalMember = tokenService.isValidateToken(token);
        if (optionalMember.isEmpty()) {
            throw new CustomException(ErrorCode.NotLogin);
        }
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
