package gift.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class ProductRequestDto {

  @NotBlank(message = "이름은 필수입니다.")
  @Size(max = 15, message = "이름은 최대 15글자입니다")
  @Pattern(regexp = "^[가-힣a-zA-Z0-9\\(\\)\\[\\]\\+\\-\\&\\/\\_ ]+$", message = "한글, 영문자, 숫자, ( ), [ ], +, -, &, /, _ 만 입력 가능(공백포함)")
  private String name;

  @NotNull(message = "가격은 필수입니다.")
  @Min(value = 0, message = "가격은 음수이면 안된다")
  private Long price;

  @NotBlank(message = "imageUrl은 필수입니다.")
  @URL(message = "유효한 URL 형식이여야합니다.(http://, https://로 시작)")
  private String imageUrl;

  private Boolean mdOk = false;

  public ProductRequestDto(String name, Long price, String imageUrl, Boolean mdOk) {
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.mdOk = mdOk;
  }

  public ProductRequestDto(String name, Long price, String imageUrl) {
    this(name, price, imageUrl, false);
  }

  public ProductRequestDto() {
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

  public Boolean getMdOk() {
    return mdOk;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setMdOk(Boolean mdOk) {
    this.mdOk = mdOk;
  }
}
