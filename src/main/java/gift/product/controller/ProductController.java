package gift.product.controller;

import gift.product.dto.ProductCreateRequestDto;
import gift.product.dto.ProductUpdateRequestDto;
import gift.product.dto.ProductCreateResponseDto;
import gift.product.dto.ProductGetResponseDto;
import gift.product.exception.ProductNotFoundException;
import gift.product.exception.UnapprovedProductException;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping
    public ResponseEntity<ProductCreateResponseDto> createProduct(
        @Valid @RequestBody ProductCreateRequestDto productCreateRequestDto) {

        return new ResponseEntity<>(productService.saveProduct(productCreateRequestDto),
            HttpStatus.CREATED);
    }

    @GetMapping
    public List<ProductGetResponseDto> getProducts() {

        return productService.findAllProducts();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductGetResponseDto> getProductById(@PathVariable Long productId) {

        return new ResponseEntity<>(productService.findProductById(productId), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProductById(@PathVariable Long productId,
        @Valid @RequestBody ProductUpdateRequestDto productUpdateRequestDto) {

        productService.updateProductById(productId, productUpdateRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long productId) {

        productService.deleteProductById(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(UnapprovedProductException.class)
    public ResponseEntity<String> handleUnapprovedProductException(UnapprovedProductException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("오류: " + ex.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(
        ProductNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("오류: " + ex.getMessage());
    }
}