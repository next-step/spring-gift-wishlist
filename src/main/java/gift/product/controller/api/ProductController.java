package gift.product.controller.api;

import gift.product.domain.Product;
import gift.product.dto.ProductDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/list")
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @PostMapping("/product/add")
    public ProductDto saveProduct(@RequestBody @Valid ProductDto productdto) {
        productService.saveProduct(productdto);
        return productdto;
    }

    @GetMapping("/product/{id}")
    public ProductDto findById(@PathVariable String id) {
        return productService.findById(id);
    }

    @PatchMapping("/product/{id}/update")
    public ProductDto updateProduct(@PathVariable String id, @RequestBody @Valid ProductDto updateProductdto) {
        productService.updateProduct(id, updateProductdto);
        return updateProductdto;
    }

    @DeleteMapping("/product/{id}/delete")
    public ProductDto deleteById(@PathVariable String id) {
        return productService.deleteProduct(id);
    }
}
