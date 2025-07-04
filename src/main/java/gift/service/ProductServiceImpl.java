package gift.service;

import gift.model.CustomPage;
import gift.entity.Product;
import gift.repository.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class ProductServiceImpl implements ProductService {
    private final static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
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
            log.error("상품 ID가 null 입니다.");
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }
        Optional<Product> product = productRepository.findById(productId);

        return product.orElseThrow(() ->
        {
            log.error("Id {}에 해당하는 상품이 존재하지 않습니다.", productId);
            return new NoSuchElementException(
                    String.format("Id %d에 해당하는 상품이 존재하지 않습니다.", productId)
            );
        });
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Product product) {
        Product updated = getById(product.getId());

        if (product.getName() != null) {
            updated.setName(product.getName());
        }

        if (product.getPrice() != null) {
            updated.setPrice(product.getPrice());
        }

        if (product.getImageUrl() != null) {
            updated.setImageUrl(product.getImageUrl());
        }

        return productRepository.save(updated);
    }

    @Override
    public Product patch(Product product) {
        Product updated = getById(product.getId());
        if (product.getName() != null) {
            updated = productRepository.updateFieldById(product.getId(), "name", product.getName());
        }
        if (product.getPrice() != null) {
            updated = productRepository.updateFieldById(product.getId(), "price", product.getPrice());
        }
        if (product.getImageUrl() != null) {
            updated = productRepository.updateFieldById(product.getId(), "image_url", product.getImageUrl());
        }
        return updated;
    }

    @Override
    public void deleteById(Long productId) {
        getById(productId); // 존재 여부 확인

        if (!productRepository.deleteById(productId)) {
            throw new NoSuchElementException(
                    String.format("Id %d에 해당하는 상품을 삭제할 수 없습니다.", productId));
        }
    }
}
