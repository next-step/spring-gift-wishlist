package gift.Entity;

// 검색으로 찾아내어 간편화된 코드
//public record Product(Long id, String name, int price, String imageUrl){}

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Product {

    private Long id;

    //NotNull -> null이 아니어야함
    //NotEmpty -> null도 안 되고, 길이 0도 안됨
    //NotBlank -> 문자열만 대상, null/빈 문자열/공백 모두 허용 안함
    // 기능 요구사항에서 공백을 포함하였으니 NotNull이 적합함
    @NotNull(message = "상품 이름은 15자 이하로 작성부탁드립니다.")
    @Size(max=15)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-\\&/_ ]*$")
    private String name;
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
}




