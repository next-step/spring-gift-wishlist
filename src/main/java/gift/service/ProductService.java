package gift.service;

import gift.entity.Product;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product productWithoutId) {
        return productRepository.save(productWithoutId);
    }

    public Optional<Product> updateProduct(Long id, Product updateRequest) {
        updateRequest.setId(id);

        if (productRepository.update(updateRequest)) {
            return Optional.empty();
        }

        return productRepository.findById(id);
    }

    public boolean deleteProduct(Long id) {
        return productRepository.deleteById(id);
    }
}
