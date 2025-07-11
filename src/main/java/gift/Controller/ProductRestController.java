package gift.Controller;

import gift.Entity.Product;
import gift.dto.ProductDao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductRestController {

    private final ProductDao productDao;

    public ProductRestController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @PostMapping("/products")
    public ResponseEntity<Product> insertProduct(@RequestBody Product product) {
        try{
            productDao.insertProduct(product);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> showAllProducts() {
        return ResponseEntity.ok(productDao.showProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> selectProduct(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(productDao.selectProduct(id));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try{
            productDao.updateProduct(id, product);
            return ResponseEntity.ok(product);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productDao.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}