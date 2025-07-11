package gift.service.product;

import static gift.entity.product.value.ProductName.FORBIDDEN_PATTERNS;

import gift.entity.member.value.Role;
import gift.entity.product.Product;
import gift.exception.custom.ProductNotFoundException;
import gift.repository.product.ProductRepository;
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
    public List<Product> getAllProducts(Role role) {
        List<Product> all = repo.findAll();
        if (role == Role.USER) {
            return all.stream()
                    .filter(p -> !p.hidden())
                    .toList();
        }
        return all;
    }

    @Override
    public Optional<Product> getProductById(Long id, Role role) {
        Optional<Product> opt = repo.findById(id);
        Product p = opt.orElseThrow(() -> new ProductNotFoundException(id));
        if (role == Role.USER && p.hidden()) {
            throw new ProductNotFoundException(id);
        }
        return Optional.of(p);
    }

    @Override
    public Product createProduct(String name, int price, String imageUrl, Role role) {
        Product newProduct = Product.of(name, price, imageUrl);
        if (role == Role.USER && isForbidden(name)) {
            newProduct = newProduct.withHidden(true);
        }
        return repo.save(newProduct);
    }

    @Override
    public Product updateProduct(Long id, String name, int price, String imageUrl, Role role) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        if (role == Role.USER && existing.hidden()) {
            throw new ProductNotFoundException(id);
        }

        Product updated = existing.withName(name)
                .withPrice(price)
                .withImageUrl(imageUrl);

        if (role == Role.USER && isForbidden(name)) {
            updated = updated.withHidden(true);
        }
        return repo.save(updated);
    }

    @Override
    public void deleteProduct(Long id, Role role) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        if (role == Role.USER && existing.hidden()) {
            throw new ProductNotFoundException(id);
        }
        repo.deleteById(id);
    }

    @Override
    public void hideProduct(Long id, Role role) {
        if (role == Role.USER) {
            throw new ProductNotFoundException(id);
        }
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        repo.save(p.withHidden(true));
    }

    @Override
    public void unhideProduct(Long id, Role role) {
        if (role == Role.USER) {
            throw new ProductNotFoundException(id);
        }
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        repo.save(p.withHidden(false));
    }

    private boolean isForbidden(String name) {
        return FORBIDDEN_PATTERNS.stream()
                .anyMatch(f -> f.matcher(name).find());
    }
}
