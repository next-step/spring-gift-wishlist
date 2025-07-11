package com.example.demo.dto.wish;

public class WishRequestDto {
  private Long productId;

  public WishRequestDto() {}
  public WishRequestDto(Long productId){
    this.productId = productId;
  }

  public Long getProductId(){
    return productId;
  }
}
