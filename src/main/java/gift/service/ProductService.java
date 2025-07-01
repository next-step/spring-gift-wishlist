package gift.service;

import gift.domain.Product;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        return toResponse(product);
    }

    public ProductResponse save(ProductRequest request) {
        Product saved = productRepository.save(new Product(null, request.getName(), request.getPrice(), request.getImageUrl()));
        return toResponse(saved);
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        product.update(request.getName(), request.getPrice(), request.getImageUrl());
        productRepository.update(product);
        return toResponse(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }
}

