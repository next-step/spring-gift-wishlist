package gift.service;

import gift.entity.Product;
import gift.entity.Product.ValidationMode;
import gift.exception.ResourceNotFoundException;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional
    public Product createProduct(Product product, ValidationMode validationMode) {
        Product created = Product.createProduct(null, product.name(), product.price(),
                product.imageUrl(), validationMode);

        return repo.save(created);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product product, ValidationMode validationMode) {
        repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다: " + id));

        Product updated =
                Product.createProduct(id, product.name(), product.price(),
                        product.imageUrl(), validationMode);
        return repo.save(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다: " + id));
        repo.deleteById(id);
    }
}
