package gift.controller;

import gift.dto.api.AddProductRequestDto;
import gift.dto.api.ModifyProductRequestDto;
import gift.dto.api.ProductResponseDto;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    
    //상품 추가 api
    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(
        @RequestBody @Valid AddProductRequestDto requestDto
    ) {
        ProductResponseDto responseDto = productService.addProduct(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    //상품 전체 조회 api
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findAllProducts() {
        List<ProductResponseDto> responseDtoList = productService.findAllProducts();
        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }
    
    //상품 단건 조회 api
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> findProductWithId(
        @PathVariable(name="productId") Long id
    ) {
        ProductResponseDto responseDto = productService.findProductWithId(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    
    //상품 전체 수정 api
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> modifyProductWithId(
        @PathVariable(name="productId") Long id,
        @RequestBody @Valid ModifyProductRequestDto requestDto
    ) {
        ProductResponseDto responseDto = productService.modifyProductWithId(id, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    
    //상품 일부 수정 api
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> modifyProductInfoWithId(
        @PathVariable(name="productId") Long id,
        @RequestBody @Valid ModifyProductRequestDto requestDto
    ) {
        ProductResponseDto responseDto = productService.modifyProductInfoWithId(id,
            requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    
    //상품 단건 삭제 api
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductWithId(
        @PathVariable(name="productId") Long id
    ) {
        productService.deleteProductWithId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
