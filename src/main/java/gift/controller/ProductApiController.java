package gift.controller;

import gift.dto.request.ProductSaveReqDTO;
import gift.dto.request.ProductUpdateReqDTO;
import gift.dto.response.ProductResDTO;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;

    @Autowired
    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResDTO> createProduct(
        @Valid
        @RequestBody
        ProductSaveReqDTO productSaveReqDTO
    ) {
        return ResponseEntity.ok(productService.save(productSaveReqDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResDTO> updateProduct(
        @PathVariable("id")
        Long id,

        @Valid
        @RequestBody
        ProductUpdateReqDTO productUpdateReqDTO
    ) {
        return ResponseEntity.ok(productService.update(id, productUpdateReqDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.ok("상품 삭제가 완료되었습니다.");
    }
}
