package gift.service;

import gift.model.CustomPage;
import gift.entity.Product;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public CustomPage<Product> getBy(int page, int size) {
        return productRepository.findAll(page, size);
    }

    @Override
    public Product getById(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }
        Optional<Product> product = productRepository.findById(productId);

        return product.orElseThrow(() ->
                new NoSuchElementException(
                        String.format("Id %d에 해당하는 상품이 존재하지 않습니다.", productId)
                ));
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Product product) {
        Product updated = getById(product.getId());

        if (product.getName() != null)
            updated.setName(product.getName());
        if (product.getPrice() != null)
            updated.setName(product.getName());
        if (product.getImageUrl() != null)
            updated.setImageUrl(product.getImageUrl());

        return productRepository.save(updated);
    }

    @Override
    public void deleteById(Long productId) {
        if (getById(productId) == null) {
            throw new NoSuchElementException(
                    String.format("Id %d에 해당하는 상품이 존재하지 않습니다.", productId));
        }

        if (!productRepository.deleteById(productId)) {
            throw new NoSuchElementException(
                    String.format("Id %d에 해당하는 상품을 삭제할 수 없습니다.", productId));
        }
    }
}
