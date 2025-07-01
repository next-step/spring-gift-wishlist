package gift.product.controller;

import gift.product.domain.Product;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody @Valid ProductRequestDto requestDto){
        Product savedProduct = productService.saveProduct(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts(){
        List<ProductResponseDto> responses = productService.getProducts()
                .stream()
                .map(product -> ProductResponseDto.productFrom(product))
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id){
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(ProductResponseDto.productFrom(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto requestDto){
        productService.update(id, requestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
