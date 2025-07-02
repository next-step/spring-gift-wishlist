package gift.service;

import gift.dto.ProductRequestDto;
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

    public Product createProduct(ProductRequestDto productRequestDto) {
        Product product = new Product(productRequestDto);
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product product = new Product(productRequestDto);
        product.setId(id);

        if (productRepository.update(product)) {
            return Optional.empty();
        }

        return productRepository.findById(id);
    }

    public boolean deleteProduct(Long id) {
        return productRepository.deleteById(id);
    }
}
