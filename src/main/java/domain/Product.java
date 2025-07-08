package domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class Product {

    private Long id;

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Size(max=15, message = "이름은 최대 15자 입력할 수 있습니다.")
    @Pattern(regexp= "^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]*$",
    message = "특수문자는 사용할 수 없습니다.")
    private String name;

    @Positive(message = "가격은 0보다 커야 합니다.")
    private int price;

    @NotBlank(message = "이미지 URL을 입력해주세요.")
    private String imageUrl;

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product() {

    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
