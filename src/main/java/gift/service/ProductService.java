package gift.service;

import gift.common.dto.request.ProductRequestDto;
import gift.common.dto.response.MessageResponseDto;
import gift.common.dto.response.ProductDto;
import gift.common.exception.CreationFailException;
import gift.common.exception.EntityNotFoundException;
import gift.domain.product.Product;
import gift.domain.product.ProductQueryOption;
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

    public MessageResponseDto<ProductDto> createProduct(ProductRequestDto body) {
        Product instance = body.toEntity();
        if (instance.involveKakao()) {
            instance.waitApproval();
            Product created = productRepository.save(instance)
                    .orElseThrow(() -> new CreationFailException("Fail to create Product"));
            return new MessageResponseDto<>(false, "카카오 관련 상품 승인 대기중", 202, ProductDto.from(created));
        }
        instance.onBoard();
        Product created = productRepository.save(instance)
                .orElseThrow(() -> new CreationFailException("Fail to create Product"));
        return new MessageResponseDto<>(true, "상품 생성 완료", 201, ProductDto.from(created));
    }

    public ProductDto getProduct(Long id, ProductQueryOption option) {
        Product result = findProduct(id);
        if (!result.isShowable(option)) {
            throw new EntityNotFoundException("Product cannot show: " + id);
        }
        return ProductDto.from(result);
    }

    public List<ProductDto> getAllProduct(ProductQueryOption option) {
        return productRepository.findAll().stream()
                .filter(p -> p.isShowable(option))
                .sorted(Comparator.comparing(Product::getId))
                .map(ProductDto::from)
                .toList();
    }

    public MessageResponseDto<ProductDto> updateProduct(Long id, ProductRequestDto body) {
        findProduct(id);
        Product instance = body.toEntity();
        if (instance.involveKakao()) {
            instance.waitApproval();
            Product updated = productRepository.update(id, instance).get();
            return new MessageResponseDto<>(false, "카카오 관련 상품 승인 대기중", 202, ProductDto.from(updated));
        }
        instance.onBoard();
        Product updated = productRepository.update(id, instance).get();
        return new MessageResponseDto<>(true, "상품 수정 완료", 200, ProductDto.from(updated));
    }

    public void deleteProduct(Long id) {
        findProduct(id);
        productRepository.delete(id);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product id {" + id + "} not found"));
    }
}
