package com.example.demo.service.product;

import com.example.demo.dto.product.ProductRequestDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.dto.product.ProductUpdateDto;
import com.example.demo.entity.Product;
import com.example.demo.repository.product.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository){
    this.productRepository = productRepository;
  }

  @Override
  public ProductResponseDto addProduct(ProductRequestDto dto) {
    Product product = new Product(
        dto.getName(),
        dto.getPrice(),
        dto.getImageUrl()
    );
    Product addProduct = productRepository.addProduct(product);
    return toDto(addProduct);
  }

  @Override
  public ProductResponseDto productFindById(Long id) {
    Product product = productRepository.productFindById(id)
                                       .orElseThrow(()-> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    return toDto(product);
  }

  @Override
  public List<ProductResponseDto> productFindAll() {
    return productRepository.productFindAll()
                            .stream()
                            .map(this::toDto)
                            .toList();
  }

  @Override
  public ProductResponseDto productUpdateById(Long id, ProductUpdateDto dto) {
    Product product = productRepository.productFindById(id)
                                       .orElseThrow(() -> new IllegalArgumentException(
            "해당 ID의 상품이 존재하지 않습니다: " + id));
    String name = dto.getName() != null ? dto.getName() : product.getName();
    int price = dto.getPrice() != null ? dto.getPrice() : product.getPrice();
    String imageUrl = dto.getImageUrl() != null ? dto.getImageUrl() : product.getImageUrl();
    product.update(name, price, imageUrl);

    productRepository.productUpdateById(product);
    return toDto(product);
  }

  @Override
  public void productDeleteById(Long id) {
    productRepository.deleteProductById(id);
  }

  private ProductResponseDto toDto(Product product){
    return new ProductResponseDto(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getImageUrl()
    );
  }
}
