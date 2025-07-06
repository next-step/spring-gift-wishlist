package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStatusService productStatusService;

    public ProductService(ProductRepository productRepository,
            ProductStatusService productStatusService) {
        this.productRepository = productRepository;
        this.productStatusService = productStatusService;
    }

    @Transactional
    public ProductResponseDto saveProduct(ProductRequestDto productRequestDto) {
        Product product = new Product(null, productRequestDto.name(),
                productRequestDto.price(), productRequestDto.imageUrl(),
                productStatusService.getProductStatus(productRequestDto.name()));

        Product savedProduct = productRepository.saveProduct(product);

        return ProductResponseDto.from(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findProduct(Long productId) {
        Product product = findProductOrThrow(productId);

        return ProductResponseDto.from(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequestDto) {
        Product product = findProductOrThrow(productId);
        product.update(
                productRequestDto.name(),
                productRequestDto.price(),
                productRequestDto.imageUrl(),
                productStatusService.getProductStatus(productRequestDto.name())
        );

        productRepository.updateProduct(product);

        return ProductResponseDto.from(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        findProductOrThrow(productId);

        productRepository.deleteProduct(productId);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAllProducts() {

        return productRepository.findAllProducts()
                                .stream()
                                .map(ProductResponseDto::from)
                                .toList();
    }

    private Product findProductOrThrow(Long productId) {
        return productRepository.findProduct(productId)
                                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
