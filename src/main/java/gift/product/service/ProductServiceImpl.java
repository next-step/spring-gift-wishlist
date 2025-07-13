package gift.product.service;

import gift.common.pagination.PageRequestDto;
import gift.common.pagination.PageResult;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = new Product(
                null,
                productRequestDto.name(),
                productRequestDto.price(),
                productRequestDto.imageUrl()
        );
        Product created = productRepository.createProduct(product);
        return ProductResponseDto.from(created);
    }

    @Override
    public PageResult<ProductResponseDto> findAllProducts(PageRequestDto pageRequestDto) {
        PageResult<Product> productPage = productRepository.findAllProducts(pageRequestDto);

        List<ProductResponseDto> content = productPage.content().stream()
                .map(ProductResponseDto::from)
                .toList();

        return new PageResult<>(
                content,
                productPage.currentPage(),
                productPage.totalPages(),
                productPage.pageSize(),
                productPage.totalElements()
        );
    }

    @Override
    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ProductResponseDto.from(product);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product product = new Product(id, productRequestDto.name(), productRequestDto.price(), productRequestDto.imageUrl());
        Product updated = productRepository.updateProduct(id, product);
        return ProductResponseDto.from(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteProduct(id);
    }
}
