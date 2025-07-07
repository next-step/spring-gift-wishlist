package com.example.demo.dto;

import com.example.demo.entity.Product;

public record ProductResponseDto(
  Long id,
  String name,
  int price,
  String imageUrl
  ){
    public ProductResponseDto(Product product){
      this(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }
}
