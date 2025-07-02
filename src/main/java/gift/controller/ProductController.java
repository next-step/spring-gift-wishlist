package gift.controller;

import gift.dto.request.ProductCreateRequestDto;
import gift.dto.request.ProductUpdateRequestDto;
import gift.dto.response.ProductCreateResponseDto;
import gift.dto.response.ProductGetResponseDto;
import gift.service.ApprovedProductService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final ApprovedProductService approvedProductService;

    public ProductController(ProductService productService,
        ApprovedProductService approvedProductService) {
        this.productService = productService;
        this.approvedProductService = approvedProductService;
    }


    @PostMapping
    public ResponseEntity<ProductCreateResponseDto> createProduct(
        @Valid @RequestBody ProductCreateRequestDto productCreateRequestDto) {

        if (productCreateRequestDto.name().contains("카카오")) {
            boolean isApprovedProduct = approvedProductService.isApprovedProductName(
                productCreateRequestDto.name());

            if (!isApprovedProduct) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "협의되지 않은 '카카오'가 포함된 상품명은 사용할 수 없습니다."
                );
            }
        }
        // TODO: ResponseStatusException이 올바른 예외인지 확인 필요

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

        productService.updateProductById(productId, productUpdateRequestDto.name(),
            productUpdateRequestDto.price(), productUpdateRequestDto.imageUrl());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long productId) {

        productService.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }
}