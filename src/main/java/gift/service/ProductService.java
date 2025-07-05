package gift.service;

import gift.dto.request.ProductRequestDto;
import gift.dto.response.ProductResponseDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public ProductResponseDto addProduct(ProductRequestDto requestDto){
        Product product = new Product(requestDto.name(), requestDto.price(), requestDto.imageUrl(), requestDto.isKakaoApprovedByMd());
        Product savedProduct = productRepository.save(product);
        return ProductResponseDto.from(savedProduct);
    }

    public List<ProductResponseDto> getProducts(){
        return productRepository.findAll()
                .stream()
                .map(ProductResponseDto::from)
                .toList();
    }

    public ProductResponseDto getProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductResponseDto.from(product);
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        Product productToUpdate = new Product(
                id,
                requestDto.name(),
                requestDto.price(),
                requestDto.imageUrl(),
                requestDto.isKakaoApprovedByMd()
        );

        Product updatedProduct = productRepository.update(productToUpdate);

        return ProductResponseDto.from(updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

