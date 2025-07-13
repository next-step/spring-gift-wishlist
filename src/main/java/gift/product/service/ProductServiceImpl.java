package gift.product.service;

import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = new Product(null, requestDto);
        long id = productRepository.createProduct(product);
        return ProductResponseDto.from(id, product);
    }

    @Override
    public List<ProductResponseDto> findAllProducts() {
        return productRepository
            .findAllProducts()
            .stream()
            .map(ProductResponseDto::from)
            .toList();
    }

    @Override
    public ProductResponseDto findProductById(Long id) {
        return ProductResponseDto.from(productRepository.findProductById(id));
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        Product product = new Product(id, requestDto);
        productRepository.updateProduct(product);
        return ProductResponseDto.from(id, product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteProduct(id);
    }
}
