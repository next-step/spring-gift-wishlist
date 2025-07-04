package gift.service;

import gift.common.dto.request.CreateProductDto;
import gift.common.dto.request.UpdateProductDto;
import gift.common.dto.response.MessageResponseDto;
import gift.common.dto.response.ProductDto;
import gift.common.exception.CreationFailException;
import gift.common.exception.EntityNotFoundException;
import gift.domain.product.Product;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MessageResponseDto<ProductDto> createProduct(CreateProductDto body) {
        Product instance = body.toEntity();
        if (instance.involveKakao()) {
            instance.waitApproval();
            var created = productRepository.save(instance)
                    .orElseThrow(() -> new CreationFailException("Fail to create Product"));
            return new MessageResponseDto<>(false, "카카오 관련 상품 승인 대기중", 202, ProductDto.from(created));
        }
        instance.onBoard();
        Product created = productRepository.save(instance)
                .orElseThrow(() -> new CreationFailException("Fail to create Product"));
        return new MessageResponseDto<>(true, "상품 생성 완료", 201, ProductDto.from(created));
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
        Product instance = body.toEntity();
        if (instance.involveKakao()) {
            instance.waitApproval();
            var updated = productRepository.update(id, instance).get();
            return new MessageResponseDto<>(false, "카카오 관련 상품 승인 대기중", 202, ProductDto.from(updated));
        }
        var updated = productRepository.update(id, instance).get();
        return new MessageResponseDto<>(true, "상품 수정 완료", 200, ProductDto.from(updated));
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
