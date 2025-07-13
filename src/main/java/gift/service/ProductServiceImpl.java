package gift.service;

import gift.domain.Product;
import gift.dto.ProductAdminRequestDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import gift.validation.ProductNameValidator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
    ProductNameValidator.validate(productRequestDto.name(), false);
    Product product = new Product(productRequestDto.name(), productRequestDto.price(),
        productRequestDto.imageUrl());
    return productRepository.createProduct(product);
  }

  public ProductResponseDto createAdminProduct(ProductAdminRequestDto productAdminRequestDto) {
    ProductNameValidator.validate(productAdminRequestDto.name(), productAdminRequestDto.kakaoConfirmed());
    Product product = new Product(productAdminRequestDto.name(), productAdminRequestDto.price(),
        productAdminRequestDto.imageUrl());
    return productRepository.createProduct(product);
  }

  public List<ProductResponseDto> searchAllProducts() {
    return productRepository.searchAllProducts();
  }

  public ProductResponseDto searchProductById(Long id) {
    Optional<Product> optionalProduct = productRepository.searchProductById(id);

    Product product = optionalProduct.orElseThrow(() ->
        new ProductNotFoundException(id)
    );

    return new ProductResponseDto(product);
  }

  public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
    ProductNameValidator.validate(productRequestDto.name(), false);
    searchProductById(id);
    Product updated = productRepository.updateProduct(
        id,
        productRequestDto.name(),
        productRequestDto.price(),
        productRequestDto.imageUrl()
    );

    return new ProductResponseDto(updated);
  }

  public ProductResponseDto updateAdminProduct(Long id, ProductAdminRequestDto productAdminRequestDto) {
    ProductNameValidator.validate(productAdminRequestDto.name(), productAdminRequestDto.kakaoConfirmed());
    searchProductById(id);
    Product updated = productRepository.updateProduct(
        id,
        productAdminRequestDto.name(),
        productAdminRequestDto.price(),
        productAdminRequestDto.imageUrl()
    );

    return new ProductResponseDto(updated);
  }

  public void deleteProduct(Long id) {
    searchProductById(id);
    productRepository.deleteProduct(id);
  }
}
