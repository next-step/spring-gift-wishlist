package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto requestDto) {
        if (requestDto == null) {
            return ResponseEntity.badRequest().build();
        }

        ProductResponseDto responseDto = productService.create(requestDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.id())
                .toUri(); // location 생성

        return ResponseEntity.created(location).body(responseDto); // 201 created 반환
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.find(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable("productId") Long productId,
            @RequestBody ProductRequestDto requestDto
    ) {
        if (requestDto == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(productService.update(productId, requestDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        List<ProductResponseDto> responseDtoList = productService.findAll(page, size, sort);
        return ResponseEntity.ok(responseDtoList);
    }
}
