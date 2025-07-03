package gift.service;

import gift.entity.Product;
import gift.exception.ResourceNotFoundException;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
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
    public Product createProduct(Product product,
            BiFunction<Product, String, Product> productCreator) {
        Product created = productCreator.apply(product, product.name());
        return repo.save(created);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product product,
            BiFunction<Product, String, Product> productUpdater) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다: " + id));

        Product updated = productUpdater.apply(existing, product.name())
                .withPrice(product.price())
                .withImageUrl(product.imageUrl());
        return repo.save(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다: " + id));
        repo.deleteById(id);
    }
}
