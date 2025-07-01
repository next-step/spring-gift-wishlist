package com.example.demo.dto;

public class ProductUpdateDto {
  private String name;
  private int price;
  private String imageUrl;

  public ProductUpdateDto() {
  }

  public ProductUpdateDto(String name, int price, String imageUrl) {
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public String getName() {
    return name;
  }

  public Integer getPrice() {
    return price;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
