package gift.service;

import gift.common.code.CustomResponseCode;
import gift.common.exception.CustomException;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.entity.Product;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String FORBIDDEN_KEYWORD = "카카오";

    private final ProductRepository productRepository;

    public ProductServiceImpl(@Qualifier("JDBC-Repo") ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse create(ProductRequest request) {
        validateForbiddenKeyword(request.name());

        Product savedProduct = productRepository.save(
            new Product(null, request.name(), request.price(), request.imageUrl()));

        return ProductResponse.from(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new CustomException(CustomResponseCode.NOT_FOUND));

        return ProductResponse.from(product);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        validateForbiddenKeyword(request.name());

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new CustomException(CustomResponseCode.NOT_FOUND));

        product.update(request.name(), request.price(), request.imageUrl());
        Product updatedProduct = productRepository.update(product);

        return ProductResponse.from(updatedProduct);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new CustomException(CustomResponseCode.NOT_FOUND);
        }

        productRepository.deleteById(id);
    }

    private void validateForbiddenKeyword(String name) {
        if (name.contains(FORBIDDEN_KEYWORD)) {
            throw new CustomException(CustomResponseCode.FORBIDDEN_KEYWORD_KAKAO);
        }
    }
}
