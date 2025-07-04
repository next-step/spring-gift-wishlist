package gift.service;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.entity.Product;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private void validateProductRequest(ProductRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.name() == null) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }
        if (request.name().length() > 15) {
            throw new IllegalArgumentException("상품 이름은 최대 15자까지 입력 가능합니다.");
        }
        if (!request.name().matches("^[a-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-&/_\\uAC00-\\uD7AF]+$")) {
            throw new IllegalArgumentException("허용되지 않은 특수 문자가 포함되었습니다.");
        }
        if (request.name().contains("카카오")) {
            throw new IllegalArgumentException("상품명에 '카카오'가 포함되었습니다. 담당자와 협의가 필요합니다.");
        }
        if (request.price() == null) {
            throw new IllegalArgumentException("가격은 필수입니다.");
        }
    }

    public ProductResponse createProduct(ProductRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        validateProductRequest(request);

        Product product = new Product(
            null,
            request.name(),
            request.price(),
            request.imageUrl()
        );
        product = productRepository.save(product);
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }

    public ProductResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(
                () -> new IllegalArgumentException("Product(id: " + productId + ") not found"));
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }

    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        validateProductRequest(request);

        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product(id: " + productId + ") not found");
        }
        Product existingProduct = product.get();

        String updatedName = existingProduct.getName();
        if (request.name() != null) {
            updatedName = request.name();
        }

        int updatedPrice = existingProduct.getPrice();
        if (request.price() != null) {
            updatedPrice = request.price();
        }

        String updatedImageUrl = existingProduct.getImageUrl();
        if (request.imageUrl() != null) {
            updatedImageUrl = request.imageUrl();
        }

        Product updatedProduct = new Product(
            productId,
            updatedName,
            updatedPrice,
            updatedImageUrl
        );
        updatedProduct = productRepository.update(updatedProduct);
        return new ProductResponse(
            updatedProduct.getId(),
            updatedProduct.getName(),
            updatedProduct.getPrice(),
            updatedProduct.getImageUrl()
        );
    }

    public void deleteProduct(Long productId) {
        if (productRepository.findById(productId).isEmpty()) {
            throw new IllegalArgumentException("Product(id: " + productId + ") not found");
        }
        productRepository.delete(productId);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
            .map(product -> new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
            ))
            .toList();
    }
}