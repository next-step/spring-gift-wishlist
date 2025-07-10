package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.exception.product.MdApprovalException;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAllProducts() {
        List<Product> products = productRepository.findAllProducts();
        return products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    public void validateMdApprovalForSave(ProductRequestDto dto) {
        if (dto.name().contains("카카오")) {
            throw new MdApprovalException("상품 이름에 ‘카카오’가 포함된 상품은 MD 승인 후 등록할 수 있습니다.");
        }
    }

    @Override
    @Transactional
    public ProductResponseDto saveProduct(ProductRequestDto dto) {
        validateMdApprovalForSave(dto);
        Product product = new Product(dto.name(), dto.price(), dto.imageUrl());
        Product savedProduct = productRepository.saveProduct(product);
        return new ProductResponseDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findProductById(id);
        return new ProductResponseDto(product);
    }

    public void validateMdApprovalForUpdate(ProductRequestDto dto, boolean mdApproved) {
        if (dto.name().contains("카카오") && !mdApproved) {
            throw new MdApprovalException("상품 이름에 ‘카카오’가 포함된 상품은 MD 승인 후 등록할 수 있습니다.");
        }
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {
        boolean mdApproved = productRepository.findMdApprovedById(id);
        validateMdApprovalForUpdate(dto, mdApproved);
        productRepository.updateProduct(id, dto.name(), dto.price(), dto.imageUrl());
        Product updatedProduct = productRepository.findProductById(id);
        return new ProductResponseDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteProduct(id);
    }
}
