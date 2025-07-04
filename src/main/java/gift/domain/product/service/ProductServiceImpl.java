package gift.domain.product.service;

import gift.common.pagination.Page;
import gift.common.pagination.Pageable;
import gift.domain.product.model.Product;
import gift.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.find(pageable);

    }

    @Override
    public Product getProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("상품을 찾을 수 없습니다.");
        }
        return productRepository.findById(id).orElseThrow(NoSuchElementException::new);

    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        if (!productRepository.existsById(product.getId())) {
            throw new NoSuchElementException("상품을 찾을 수 없습니다.");
        }
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("상품을 찾을 수 없습니다.");
        }
        productRepository.deleteById(id);
    }
    

}
