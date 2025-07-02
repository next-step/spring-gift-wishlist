package com.example.demo.dto;

import com.example.demo.validation.NoKakao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductUpdateDto {

  @Size(max = 15, message = "상품 이름은 15자 이하로 입력해주세요.")
  @Pattern(
      regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$",
      message = "상품 이름에는 (), [], +, -, &, /, _ 외 특수문자는 사용할 수 없습니다."
  )
  @NoKakao
  private String name;

  @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
  private int price;

  @Size(max = 255, message = "이미지 URL은 255자 이하로 입력해주세요.")
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
