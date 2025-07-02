package gift.entity;

import gift.dto.ProductResponseDto;


public class Product {

  private Long id;
  private String name;
  private int price;
  private String imageUrl;

  public Product(Long id, String name, int price, String imageUrl) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public ProductResponseDto toDto() {
    return new ProductResponseDto(id, name, price, imageUrl);
  }

  public Long getId() {
    return id;
  }


}
