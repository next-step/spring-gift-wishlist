package gift.Controller;

import gift.Entity.Product;
import gift.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductRestController {

    private final ProductDto productDto;

    public ProductRestController(ProductDto productDto) {
        this.productDto = productDto;
    }

    @PostMapping("/products")
    public ResponseEntity<Product> insertProduct(@RequestBody Product product) {
        try{
            productDto.insertProduct(product);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> showAllProducts() {
        return ResponseEntity.ok(productDto.showProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> selectProduct(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(productDto.selectProduct(id));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try{
            productDto.updateProduct(id, product);
            return ResponseEntity.ok(product);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productDto.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
