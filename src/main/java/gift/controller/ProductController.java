package gift.controller;

import gift.dto.ProductAddRequestDto;
import gift.dto.ProductUpdateRequestDto;
import gift.dto.ProductResponseDto;
import gift.exception.InvalidProductException;
import gift.service.ProductService;
import gift.service.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(
            @Valid @RequestBody ProductAddRequestDto requestDto, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new InvalidProductException(bindingResult.getFieldError().getDefaultMessage());
        }
        productService.addProduct(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findProductById(
            @PathVariable Long id
    ) {
        ProductResponseDto responseDto = productService.findProductById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProductById(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequestDto requestDto, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new InvalidProductException(bindingResult.getFieldError().getDefaultMessage());
        }
        productService.updateProductById(id, requestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(
            @PathVariable Long id
    ) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
