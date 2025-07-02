package gift.service;

import gift.dto.PageResponseDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.repository.JdbcProductRepository;
import gift.repository.ProductRepositoryInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements ProductServiceInterface {

    private final JdbcProductRepository productRepository;

    public ProductService(@Qualifier("jdbcProductRepository") JdbcProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseDto addProduct(ProductRequestDto requestDto) {

        String name = requestDto.getName();
        if (name != null && name.contains("카카오")) {
            boolean approved = productRepository.isApprovedKakao(name);
            if (!approved) {
                throw new IllegalArgumentException("'카카오'가 포함된 상품명은 담당 MD 승인 후 등록할 수 있습니다.");
            }
        }

        Product product = new Product(
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getImageUrl()
        );

        Product addedProduct = productRepository.addProduct(product);

        return new ProductResponseDto(addedProduct.getId(), addedProduct.getName(), addedProduct.getPrice(), addedProduct.getImageUrl());
    }

    @Override
    public PageResponseDto getPageProducts(int page, int pageSize) {

        int totalProducts = productRepository.countAllProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        if (totalPages == 0){
            totalPages = 1;
        }

        int fromIndex = Math.max(0, (page - 1) * pageSize);

        List<Product> productList = productRepository.findProductsByPage(fromIndex, pageSize);
        List<ProductResponseDto> products = new ArrayList<>();
        for (Product product : productList) {
            products.add(new ProductResponseDto(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl()
            ));
        }

        return new PageResponseDto(page, totalPages, products);
    }

    @Override
    public List<ProductResponseDto> findAllProducts() {
        List<Product> productList = productRepository.findAllProducts();
        List<ProductResponseDto> products = new ArrayList<>();
        for (Product product : productList) {
            products.add(new ProductResponseDto(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl()
            ));
        }
        return products;
    }

    @Override
    public Optional<ProductResponseDto> findProductById(Long id) {
        return productRepository.findProductById(id)
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImageUrl()
                ));
    }

    @Override
    public Optional<ProductResponseDto> updateProduct(Long id, ProductRequestDto requestDto) {

        String name = requestDto.getName();
        if (name != null && name.contains("카카오")) {
            boolean approved = productRepository.isApprovedKakao(name);
            if (!approved) {
                throw new IllegalArgumentException("'카카오'가 포함된 상품명은 담당 MD 승인 후 등록할 수 있습니다.");
            }
        }

        Product product = new Product(
                id,
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getImageUrl()
        );

        return productRepository.updateProduct(id, product)
                .map(updated -> new ProductResponseDto(
                        updated.getId(),
                        updated.getName(),
                        updated.getPrice(),
                        updated.getImageUrl()
                ));
    }

    @Override
    public void deleteProduct(Long id) {
        boolean deleted = productRepository.deleteProduct(id);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public int countAllProducts() {
        return productRepository.countAllProducts();
    }

}
