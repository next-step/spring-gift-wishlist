package gift.repository;

import gift.dto.api.ProductResponseDto;
import gift.entity.Product;
import java.util.List;

public interface ProductRepository {
    
    ProductResponseDto addProduct(Product product);
    
    List<ProductResponseDto> findAllProducts();
    
    Product findProductWithId(Long id);
    
    ProductResponseDto modifyProductWithId(Long id, Product newProduct);
    
    void deleteProductWithId(Long id);
}
