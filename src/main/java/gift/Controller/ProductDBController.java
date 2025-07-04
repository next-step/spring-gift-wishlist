package gift.Controller;

import gift.Entity.Product;
import gift.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductDBController {

    private final ProductService productService;

    public ProductDBController( ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<Product> insertProduct(@RequestBody Product product) {
        try{
            productService.validateProductException(product);
            productService.insertProduct(product);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> showAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> selectProduct(@PathVariable Long id) {
        try{
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try{
            productService.validateProductException(product);
            productService.updateProduct(id, product);
            return ResponseEntity.ok(product);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok().build();
    }
}
