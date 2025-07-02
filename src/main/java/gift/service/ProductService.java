package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.repository.ProductRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDto saveProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        if (requestDto.name().contains("카카오")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다.");
        }
        Product product = new Product(requestDto.name(), requestDto.price(), requestDto.imageUrl());
        Product savedProduct = productRepository.saveProduct(product);

        return new ProductResponseDto(savedProduct);
    }

    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findAllProducts();
    }

    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ProductResponseDto(product);
    }

    public ProductResponseDto updateProduct(Long id, String name, Integer price, String url) {
        if (name.contains("카카오")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다.");
        }
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        product.update(name, price, url);
        productRepository.updateProduct(product);

        return new ProductResponseDto(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        productRepository.deleteProduct(id);

    }
}
