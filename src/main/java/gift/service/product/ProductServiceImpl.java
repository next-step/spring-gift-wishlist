package gift.service.product;

import static gift.entity.product.value.ProductName.FORBIDDEN_PATTERNS;

import gift.entity.product.Product;
import gift.exception.ProductNotFoundExection;
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
    public List<Product> getAllProducts(String role) {
        List<Product> all = repo.findAll();
        if ("USER".equals(role)) {
            // USER는 forbidden 패턴 상품 제외
            return all.stream()
                    .filter(Product::hidden)
                    .toList();
        }
        return all;
    }

    @Override
    public Optional<Product> getProductById(Long id, String role) {
        Optional<Product> opt = repo.findById(id);
        Product p = opt.orElseThrow(() -> new ProductNotFoundExection(id));
        if ("USER".equals(role) && p.hidden()) {
            throw new ProductNotFoundExection(id);
        }
        return Optional.of(p);
    }

    @Override
    public Product createProduct(String name, int price, String imageUrl, String role) {
        if ("USER".equals(role) && isForbidden(name)) {
            throw new IllegalArgumentException(
                    "USER 권한으로는 '카카오'가 포함된 상품을 생성할 수 없습니다.");
        }
        // 엔티티 static factory 사용
        Product newProduct = Product.of(name, price, imageUrl);
        return repo.save(newProduct);
    }

    @Override
    public Product updateProduct(Long id, String name, int price, String imageUrl, String role) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundExection(id));
        if ("USER".equals(role) && existing.hidden()) {
            throw new ProductNotFoundExection(id);
        }
        if ("USER".equals(role) && isForbidden(name)) {
            throw new IllegalArgumentException(
                    "USER 권한으로는 '카카오'가 포함된 상품이름으로 수정할 수 없습니다.");
        }

        Product updated = existing.withName(name)
                .withPrice(price)
                .withImageUrl(imageUrl);
        return repo.save(updated);
    }

    @Override
    public void deleteProduct(Long id, String role) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundExection(id));
        if ("USER".equals(role) && existing.hidden()) {
            throw new ProductNotFoundExection(id);
        }
        repo.deleteById(id);
    }

    @Override
    public void hideProduct(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundExection(id));
        repo.save(p.withHidden(true));
    }

    @Override
    public void unhideProduct(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundExection(id));
        repo.save(p.withHidden(false));
    }

    private boolean isForbidden(String name) {
        return FORBIDDEN_PATTERNS.stream()
                .anyMatch(f -> f.matcher(name).find());
    }
}
