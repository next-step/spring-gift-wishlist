package com.example.demo.service.product;

import com.example.demo.dto.product.ProductRequestDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.dto.product.ProductUpdateDto;
import java.util.List;

public interface ProductService {

  ProductResponseDto addProduct(ProductRequestDto dto);
  ProductResponseDto productFindById(Long id);
  List<ProductResponseDto> productFindAll();
  ProductResponseDto productUpdateById(Long id, ProductUpdateDto dto);
  void productDeleteById(Long id);
}
