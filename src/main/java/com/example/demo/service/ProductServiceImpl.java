package com.example.demo.service;

import com.example.demo.dto.ProductRequestDto;
import com.example.demo.dto.ProductResponseDto;
import com.example.demo.dto.ProductUpdateDto;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductJdbcClientRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

  private final ProductJdbcClientRepository productJdbcClientRepository;

  public ProductServiceImpl(ProductJdbcClientRepository productJdbcClientRepository){
    this.productJdbcClientRepository = productJdbcClientRepository;
  }

  @Override
  public ProductResponseDto addProduct(ProductRequestDto dto) {
    Product product = new Product(
        dto.getName(),
        dto.getPrice(),
        dto.getImageUrl()
    );
    Product addProduct = productJdbcClientRepository.addProduct(product);
    return toDto(addProduct);
  }

  @Override
  public ProductResponseDto productFindById(Long id) {
    Product product = productJdbcClientRepository.productFindById(id)
        .orElseThrow(()-> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    return toDto(product);
  }

  @Override
  public List<ProductResponseDto> productFindAll() {
    return productJdbcClientRepository.productFindAll()
        .stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  public ProductResponseDto productUpdateById(Long id, ProductUpdateDto dto) {
    Product product = productJdbcClientRepository.productFindById(id)
        .orElseThrow(() -> new IllegalArgumentException(
            "해당 ID의 상품이 존재하지 않습니다: " + id));
    String name = dto.getName() != null ? dto.getName() : product.getName();
    int price = dto.getPrice() != null ? dto.getPrice() : product.getPrice();
    String imageUrl = dto.getImageUrl() != null ? dto.getImageUrl() : product.getImageUrl();
    product.update(name, price, imageUrl);

    productJdbcClientRepository.productUpdateById(product);
    return toDto(product);
  }

  @Override
  public void productDeleteById(Long id) {
    productJdbcClientRepository.deleteProductById(id);

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
