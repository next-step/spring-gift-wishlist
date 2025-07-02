package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.exception.NotFoundByIdException;
import gift.exception.RequestNotValidException;
import gift.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/products")
    public ResponseEntity<String> createProduct(
            @Validated @RequestBody ProductRequestDto productRequestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestNotValidException(bindingResult);
        }

        Long productId = productService.saveProduct(productRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created id: " + productId);
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @Validated @RequestBody ProductRequestDto productRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new RequestNotValidException(bindingResult);
        }

        productService.updateProduct(productId, productRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @ExceptionHandler(RequestNotValidException.class)
    public ResponseEntity<String> handleRequestNotValidException(RequestNotValidException e) {
        String message = e.getMessage();
        log.trace(message);
        return ResponseEntity.badRequest()
                .body(message);
    }

    @ExceptionHandler(NotFoundByIdException.class)
    public ResponseEntity<String> handleNotFoundByIdException(NotFoundByIdException e) {
        log.trace(e.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Not Found by ID");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseError(HttpMessageNotReadableException e) {
        log.trace(e.getMessage());
        return ResponseEntity.badRequest().body("Invalid Request");
    }
}
