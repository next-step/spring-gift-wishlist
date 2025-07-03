package gift.service;

import gift.domain.Product;
import gift.dto.ProductRequest;
import gift.exception.ErrorCode;
import gift.exception.InvalidProductException;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceAdmin{

    private final ProductRepository productRepository;

    public ProductServiceAdmin(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductByIdAdmin(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new InvalidProductException(ErrorCode.NOT_EXISTS_PRODUCT));
    }

    public List<Product> getProductListAdmin() {
        return productRepository.findAll();
    }

    public void insertAdmin(Product product) {
        productRepository.insert(product);
    }

    public void updateAdmin(ProductRequest request) {
        productRepository.findById(request.id())
            .orElseThrow(() -> new InvalidProductException(ErrorCode.NOT_EXISTS_PRODUCT));

        productRepository.update(request);
    }

    public void deleteByIdAdmin(Long productId) {
        productRepository.findById(productId)
            .orElseThrow(() -> new InvalidProductException(ErrorCode.NOT_EXISTS_PRODUCT));

        productRepository.deleteById(productId);
    }

}
