package gift.service;

import gift.domain.Product;
import gift.dto.ProductMapper;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.exception.ErrorCode;
import gift.exception.InvalidProductException;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId);

        return ProductResponse.of(product.getName(), product.getPrice(), product.getImageUrl());
    }

    public Long insert(ProductRequest request) {
        if (request.name().contains("카카오")) {
            throw new InvalidProductException(ErrorCode.INVALID_KAKAO_NAME);
        }

        Long id = productRepository.insert(ProductMapper.toEntity(request));
        return id;
    }

    public void update(ProductRequest request) {
        productRepository.findById(request.id());

        if (request.name().contains("카카오")) {
            throw new InvalidProductException(ErrorCode.INVALID_KAKAO_NAME);
        }

        productRepository.update(request);
    }

    public void deleteById(Long productId) {
        productRepository.findById(productId);

        productRepository.deleteById(productId);
    }

    public List<ProductResponse> getProductList() {
        return productRepository.findAll().stream()
            .map(ProductMapper::toResponse)
            .toList();
    }

}
