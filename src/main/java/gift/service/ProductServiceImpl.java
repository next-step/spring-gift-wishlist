package gift.service;

import gift.dto.request.ProductCreateRequestDto;
import gift.dto.request.ProductUpdateRequestDto;
import gift.dto.response.ProductCreateResponseDto;
import gift.dto.response.ProductGetResponseDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductCreateResponseDto saveProduct(ProductCreateRequestDto productCreateRequestDto) {

        Product product = new Product(productCreateRequestDto.name(),
            productCreateRequestDto.price(), productCreateRequestDto.imageUrl());

        productRepository.saveProduct(product);

        return new ProductCreateResponseDto(product);
    }

    @Override
    public List<ProductGetResponseDto> findAllProducts() {
        return productRepository.findAllProducts();
    }

    @Override
    public ProductGetResponseDto findProductById(Long productId) {
        return productRepository.findProductById(productId)
            .map(ProductGetResponseDto::new)
            .orElseThrow(
                () -> new ProductNotFoundException("상품이 존재하지 않습니다. productId = " + productId)
            );
    }

    @Override
    public void updateProductById(Long productId, ProductUpdateRequestDto productUpdateRequestDto) {

        Product product = new Product(productId, productUpdateRequestDto.name(),
            productUpdateRequestDto.price(), productUpdateRequestDto.imageUrl());

        productRepository.updateProductById(product);
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.deleteProductById(productId);
    }
}
