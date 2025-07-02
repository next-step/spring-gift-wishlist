package gift.service;

import gift.dto.CreateProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateProductRequestDto;
import gift.entity.Product;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDto> findAllProducts() {

        return productRepository.findAllProducts()
                .stream()
                .map(ProductResponseDto::from)
                .toList();
    }

    public ProductResponseDto findProductById(Long id) {
        Product findProduct = productRepository.findProductByIdOrElseThrow(id);

        return ProductResponseDto.from(findProduct);
    }

    @Transactional
    public ProductResponseDto createProduct(CreateProductRequestDto requestDto) {
        if (requestDto.getName().contains("카카오") && !requestDto.getIsMdApproved()) {
            throw new IllegalArgumentException("상품 이름에 '카카오'를 포함하려면 MD 승인이 필요합니다.");
        }

        Product createdProduct = productRepository.createProduct(
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getImageUrl()
        );

        return ProductResponseDto.from(createdProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product findProduct = productRepository.findProductByIdOrElseThrow(id);
        productRepository.deleteProduct(id);
    }

    @Transactional
    public void updateProduct(UpdateProductRequestDto requestDto) {
        if (requestDto.getName().contains("카카오") && !requestDto.getIsMdApproved()) {
            throw new IllegalArgumentException("상품 이름에 '카카오'를 포함하려면 MD 승인이 필요합니다.");
        }

        Product findProduct = productRepository.findProductByIdOrElseThrow(requestDto.getId());
        findProduct.setName(requestDto.getName());
        findProduct.setPrice(requestDto.getPrice());
        findProduct.setImageUrl(requestDto.getImageUrl());
        productRepository.updateProduct(findProduct);
    }
}
