package gift.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class Product {

  private Long id;
  
  @NotBlank(message = "이름은 필수입니다.")
  @Size(max = 15, message = "이름은 최대 15글자입니다")
  @Pattern(regexp = "^[가-힣a-zA-Z0-9\\(\\)\\[\\]\\+\\-\\&\\/\\_ ]+$", message = "한글, 영문자, 숫자, ( ), [ ], +, -, &, /, _ 만 입력 가능(공백포함)")
  @Pattern(regexp = "^(?!.*카카오).*$", message = "상품 이름에 '카카오'가 포함되어 있습니다. 담당 MD와 협의가 필요합니다.")
  private String name;

  @NotNull(message = "가격은 필수입니다.")
  @Min(value = 0, message = "가격은 음수이면 안된다")
  private Long price;

  @NotBlank(message = "imageUrl은 필수입니다.")
  @URL(message = "유효한 URL 형식이여야합니다.(http://, https://로 시작)")
  private String imageUrl;

  public Product(Long id, String name, Long price, String imageUrl) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public Product(String name, Long price, String imageUrl) {
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public Product() {
  }
  //json을 객체로 바꾸기 위한 기본 생성자

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
