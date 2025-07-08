package gift.service;

import static gift.entity.product.value.ProductName.FORBIDDEN_PATTERNS;

import gift.entity.product.Product;
import gift.exception.ProductNotFoundExection;
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
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return Optional.ofNullable(repo.findById(id))
                .orElseThrow(() -> new ProductNotFoundExection(id));
    }

    @Override
    public Product createProduct(String name, int price, String imageUrl) {
        Product newProduct = Product.of(name, price, imageUrl);
        if (isForbidden(name)) {
            newProduct = newProduct.withHidden(true);
        }

        return repo.save(newProduct);
    }

    @Override
    public Product updateProduct(Long id, String name, int price, String imageUrl) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundExection(id));

        Product updatedProduct = existing.withName(name)
                .withPrice(price)
                .withImageUrl(imageUrl);
        if (isForbidden(name)) {
            updatedProduct = updatedProduct.withHidden(true);
        }

        return repo.save(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!repo.existsById(id)) {
            throw new ProductNotFoundExection(id);
        }
        repo.deleteById(id);
    }

    @Override
    public void hideProduct(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundExection(id));
        p = p.withHidden(true);
        repo.save(p);
    }

    @Override
    public void unhideProduct(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundExection(id));
        p = p.withHidden(false);
        repo.save(p);
    }

    private boolean isForbidden(String name) {
        return FORBIDDEN_PATTERNS.stream()
                .anyMatch(p -> p.matcher(name).find());
    }
}
