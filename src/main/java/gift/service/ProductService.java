package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
        return new ProductResponseDto(product);
    }

    public List<ProductResponseDto> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    public ProductResponseDto addProduct(ProductRequestDto requestDto) {
        validateProductName(requestDto.getName());
        Product product = Product.from(requestDto);
        Product savedProduct = productRepository.save(product);
        return new ProductResponseDto(savedProduct);
    }


    public void updateProduct(Long id, ProductRequestDto requestDto) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }

        validateProductName(requestDto.getName());

        Product updatedProduct = new Product(id, requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl());
        productRepository.update(id, updatedProduct);
    }


    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }

        productRepository.delete(id);
    }

    private void validateProductName(String name) {
        if (name.contains("카카오")) {
            throw new InvalidProductNameException("상품 이름에 '카카오'를 포함할 수 없습니다. 담당 MD와 협의 필요");
        }
    }
}
