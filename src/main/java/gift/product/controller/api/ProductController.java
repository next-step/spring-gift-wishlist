package gift.product.controller.api;

import gift.product.domain.Product;
import gift.product.dto.ProductPatchRequestDto;
import gift.product.dto.ProductSaveRequestDto;
import gift.product.dto.ResponseDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/list")
    public ResponseEntity<List<ResponseDto>> findAll() {
        List<ResponseDto> responseDtoList = productService.findAll()
                .stream()
                .map(ResponseDto::new)
                .toList();
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping("/product/add")
    public ResponseEntity<ResponseDto> saveProduct(@RequestBody @Valid ProductSaveRequestDto productSaveRequestDto) {
        Product product =  productService.saveProduct(productSaveRequestDto);
        return ResponseEntity
                .created(URI.create("/api/product/" + product.getId()))
                .body(new ResponseDto(product));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable UUID id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(new ResponseDto(product));
    }

    @PatchMapping("/product/{id}/update")
    public ResponseEntity<ResponseDto> updateProduct(@PathVariable UUID id, @RequestBody @Valid ProductPatchRequestDto productPatchRequestDto) {
        Product product = productService.updateProduct(id, productPatchRequestDto);
        return ResponseEntity.ok(new ResponseDto(product));
    }

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
