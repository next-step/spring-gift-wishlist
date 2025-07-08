package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.exception.InvalidProductNameException;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    // 의존성 고정
    private final ProductRepository productRepository;
    private final List<String> forbiddenWords; // MD 협의 단어 목록

    // 의존성 주입
    public ProductService(ProductRepository productRepository, @Value("${forbidden_words}") String forbiddenWordsProp) {
        this.productRepository = productRepository;
        this.forbiddenWords = Arrays.stream(forbiddenWordsProp.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public ProductResponseDto findProductById(Long id){
        Product product = productRepository.findProductById(id);
        return new ProductResponseDto(product);
    }

    public List<ProductResponseDto> findAllProduct(){
        return productRepository.findAllProducts().stream().map(ProductResponseDto::new).collect(Collectors.toList());
    }

    public ProductResponseDto saveProduct(ProductRequestDto requestDto){
        List<String> matched = forbiddenWords.stream().filter(requestDto.name()::contains).toList();
        if(!matched.isEmpty()){ // 금지 단어 포함돼있을 경우 예외 던지기
            throw new InvalidProductNameException(matched);
        }
        return new ProductResponseDto(productRepository.saveProduct(new Product(requestDto)));
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto){
        List<String> matched = forbiddenWords.stream().filter(requestDto.name()::contains).toList();
        if(!matched.isEmpty()){ // 금지 단어 포함돼있을 경우 예외 던지기
            throw new InvalidProductNameException(matched);
        }
        boolean flag = productRepository.updateProduct(id, new Product(requestDto));
        
        // 수정됐는지 검증
        if(!flag) {
            throw new ProductNotFoundException(id);
        }

        Product product = productRepository.findProductById(id);
        return new ProductResponseDto(product);
    }

    public void deleteProduct(Long id){
        boolean flag = productRepository.deleteProduct(id);
        if(!flag) {
            throw new ProductNotFoundException(id);
        }
    }
}
