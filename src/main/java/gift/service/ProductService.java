package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateProductRequestDto;
import gift.entity.Product;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        Product product = new Product(
                productRequestDto.name(),
                productRequestDto.price(),
                productRequestDto.imageUrl()
        );

        Product saveProduct = productRepository.addProduct(product);
        return new ProductResponseDto(
                saveProduct.getId(),
                saveProduct.getName(),
                saveProduct.getPrice(),
                saveProduct.getImageUrl()
        );
    }

    public List<ProductResponseDto> findAllProduct() {
        return productRepository.findAllProduct().stream()
            .map(p -> new ProductResponseDto(p.getId(), p.getName(), p.getPrice(), p.getImageUrl()))
            .collect(Collectors.toList());
    }

    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findProductById(id);
        if (product == null) {
            throw new NoSuchElementException("Invalid id = " + id);
        }
        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }

    public ProductResponseDto updateProduct(UpdateProductRequestDto productRequestDto) {
        Product product = productRepository.findProductById(productRequestDto.id());
        if (product == null) {
            throw new NoSuchElementException("Invalid id = " + productRequestDto.id());
        }
        product.updateProduct(productRequestDto.name(), productRequestDto.price(), productRequestDto.imageUrl());
        Product updateProduct = productRepository.updateProduct(product);

        return new ProductResponseDto(
            updateProduct.getId(),
            updateProduct.getName(),
            updateProduct.getPrice(),
            updateProduct.getImageUrl()
        );
    }


    public void deleteProduct(Long id) {
        Product product = productRepository.findProductById(id);
        if (product == null) {
            throw new NoSuchElementException("Invalid id = " + id);
        }
        productRepository.deleteProduct(id);
    }
}
