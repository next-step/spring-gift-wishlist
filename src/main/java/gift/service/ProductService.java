package gift.service;

import gift.dto.request.CreateProductDto;
import gift.dto.request.UpdateProductDto;
import gift.dto.response.MessageResponseDto;
import gift.dto.response.ProductDto;
import gift.entity.Product;
import gift.exception.CreationFailException;
import gift.exception.EntityNotFoundException;
import gift.repository.ProductRepository;
import gift.validator.ProductValidator;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductValidator validator;

    public ProductService(ProductRepository productRepository, ProductValidator validator) {
        this.productRepository = productRepository;
        this.validator = validator;
    }

    public MessageResponseDto<ProductDto> createProduct(CreateProductDto body) {
        Product instance = body.toEntity();
        if (!validator.validate(instance)) {
            // 승인 대기 큐에 추가
            return new MessageResponseDto<>(false, "카카오 관련 상품 승인 대기중", 202, null);
        }
        Product created = productRepository.save(instance)
                .orElseThrow(() -> new CreationFailException("Fail to create Product"));
        var data = ProductDto.from(created);
        return new MessageResponseDto<>(true, "상품 생성 완료", 201, data);
    }

    public ProductDto getProduct(Long id) {
        Product result = findProduct(id);
        return ProductDto.from(result);
    }

    public List<ProductDto> getAllProduct() {
        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getId))
                .map(ProductDto::from)
                .toList();
    }

    public MessageResponseDto<ProductDto> updateProduct(Long id, UpdateProductDto body) {
        findProduct(id);
        Product instance = body.toEntity(id);
        if (!validator.validate(instance)) {
            // 승인 대기 큐에 추가
            deleteProduct(id);
            return new MessageResponseDto<>(false, "카카오 관련 상품 승인 대기중", 202, null);
        }
        Product product = productRepository.update(id, instance).get();
        var data = ProductDto.from(product);
        return new MessageResponseDto<>(true, "상품 수정 완료", 200, data);
    }

    public void deleteProduct(Long id) {
        findProduct(id);
        productRepository.delete(id);
    }

    private Product findProduct(Long id) {
        var result = productRepository.findById(id);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("Product id {" + id + "} not found");
        }
        return result.get();
    }
}
