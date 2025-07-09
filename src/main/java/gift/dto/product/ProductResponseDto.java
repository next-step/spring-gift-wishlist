package gift.dto.product;

import gift.entity.Product;

public class ProductResponseDto {

  private Long id;
  private String name;
  private Long price;
  private String imageUrl;

  public ProductResponseDto() {
  }
  //기본 생성자가 있어야지 json에서 자바 객체로 생성되는 것 같다.

  public ProductResponseDto(Product product) {
    this.id = product.getId();
    this.name = product.getName();
    this.price = product.getPrice();
    this.imageUrl = product.getImageUrl();
  }


  public ProductResponseDto(Long id, String name, Long price, String imageUrl) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Long getPrice() {
    return price;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
