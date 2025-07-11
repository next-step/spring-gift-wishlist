package com.example.demo.service;

import com.example.demo.dto.ProductRequestDto;
import com.example.demo.dto.ProductResponseDto;
import com.example.demo.dto.ProductUpdateDto;
import java.util.List;

public interface ProductService {

  ProductResponseDto addProduct(ProductRequestDto dto);
  ProductResponseDto productFindById(Long id);
  List<ProductResponseDto> productFindAll();
  ProductResponseDto productUpdateById(Long id, ProductUpdateDto dto);
  void productDeleteById(Long id);
}
