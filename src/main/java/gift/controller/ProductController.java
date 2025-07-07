package gift.controller;

import gift.dto.CreateProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.service.ProductService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    private final String key = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody CreateProductRequestDto requestDto,
            @RequestHeader(value = "Authorization") String token) {
        Optional<Member> optionalMember = isValidateToken(token);
        if (optionalMember.isEmpty()) {
            throw new CustomException(ErrorCode.NotLogin);
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
        Optional<Member> optionalMember = isValidateToken(token);
        if (optionalMember.isEmpty()) {
            throw new CustomException(ErrorCode.NotLogin);
        }

        return new ResponseEntity<>(productService.updateProductById(id, requestDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization") String token) {
        Optional<Member> optionalMember = isValidateToken(token);
        if (optionalMember.isEmpty()) {
            throw new CustomException(ErrorCode.NotLogin);
        }
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Optional<Member> isValidateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(key.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            Member find = new Member(null, claims.get("email", String.class), null,
                    claims.get("role", String.class));
            return Optional.of(find);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
