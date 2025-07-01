package gift.entity;


import gift.dto.response.ProductResponseDto;
import jakarta.validation.constraints.Max;


public record Product(
    long productId,
    String name,
    int price,
    String imageURL) {



  public ProductResponseDto toResponseDto() {
    return new ProductResponseDto(productId, name, price, imageURL);
  }
}