package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductStatusPatchRequestDto;
import gift.entity.Product;
import gift.exception.NotFoundByIdException;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long saveProduct(ProductRequestDto productRequestDto) {
        return productRepository.saveProduct(
            productRequestDto.name(),
            productRequestDto.price(),
            productRequestDto.imageUrl(),
            getStatus(productRequestDto)
        );
    }

    public void deleteProductById(Long id) {
        productRepository.deleteProductById(id);
    }

    public void updateProduct(Long id, ProductRequestDto productUpdateDto) {
        productRepository.updateProduct(
                productUpdateDto.toEntity(
                        id,
                        getStatus(productUpdateDto))
        );
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findApprovedProducts() {
        return productRepository.findAllProducts().stream()
                .filter(product -> product.status() == Product.Status.APPROVED)
                .map(ProductResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findAllProducts().stream()
                .map(ProductResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findProductById(Long id) {
        return productRepository.findProductById(id)
                .map(ProductResponseDto::new)
                .orElseThrow(() -> new NotFoundByIdException("Not Found by id: " + id));
    }

    private Product.Status getStatus(ProductRequestDto productRequestDto) {
        if (productRequestDto.name().contains("카카오")) {
            return Product.Status.PENDING;
        } else {
            return Product.Status.APPROVED;
        }
    }

    public void updateProductStatus(Long productId, ProductStatusPatchRequestDto statusPatchRequestDto) {
        productRepository.updateStatus(productId, statusPatchRequestDto.status());
    }
}
