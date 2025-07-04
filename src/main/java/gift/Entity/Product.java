package gift.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Product {

    //MD와 협의 여부 파악
    private boolean MDapproved;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //NotNull -> null이 아니어야함
    //NotEmpty -> null도 안 되고, 길이 0도 안됨
    //NotBlank -> 문자열만 대상, null/빈 문자열/공백 모두 허용 안함
    // 기능 요구사항에서 공백을 포함하였으니 NotNull이 적합함
    @NotNull
    @Size(max=15, message = "상품 이름은 15자 이하로 작성부탁드립니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-\\&/_ ]*$")
    private String name;

    @NotNull
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private int price;

    private String imageUrl;

    public Product() {

    }

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }

    //setter 추가
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(int price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // MD 확인여부 getter와 setter
    public boolean getMDapproved() { return MDapproved; }
    public void setMDapproved(boolean MDapproved) { this.MDapproved = MDapproved; }
}




