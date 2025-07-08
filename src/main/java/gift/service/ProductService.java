package gift.service;

import gift.domain.Product;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository storage;

    public ProductService(ProductRepository storage) {
        this.storage = storage;
    }

    public List<Product> findAll() {
        return storage.getProducts();
    }

    public Optional<Product> findById(Long id) {
        return storage.findById(id);
    }

    public Product create(Product product) {
        validateBusinessRules(product);
        return storage.save(product);
    }

    public void update(Long id, Product updated) {
        validateBusinessRules(updated);
        storage.update(id, updated);
    }

    public void delete(Long id) {
        storage.delete(id);
    }

    private void validateBusinessRules(Product product) {
        if (product.getName() != null && product.getName().contains("카카오")) {
            throw new IllegalArgumentException("상품명에 '카카오'가 포함되어 있습니다. 담당자 확인이 필요합니다.");
        }
    }
}
