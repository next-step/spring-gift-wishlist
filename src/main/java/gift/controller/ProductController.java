package gift.controller;

import gift.dto.RequestDto;
import gift.dto.ResponseDto;
import jakarta.validation.Valid;
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

import gift.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. 상품 추가
    @PostMapping
    public ResponseEntity<ResponseDto> createProduct(@Valid @RequestBody RequestDto dto) {

        ResponseDto response = productService.create(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 2. 상품 조회
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getProduct(@PathVariable Long id) {
        ResponseDto response = productService.findById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 3. 상품 수정

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateProduct(@Valid
    @PathVariable Long id,
            @RequestBody RequestDto dto
    ) {
        ResponseDto response = productService.update(id, dto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 4. 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}