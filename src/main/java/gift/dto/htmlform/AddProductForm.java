package gift.dto.htmlform;

import jakarta.validation.constraints.Pattern;

public class AddProductForm {
    
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]{1,15}$",
        message = "이름은 한글, 영문, 숫자, 공백, (), [], +, -, &, /, _ 만 포함할 수 있으며 최대 15자까지 입력 가능합니다."
    )
    private String name;
    private Long price;
    private String imageUrl;
    
    public AddProductForm() {
    }
    
    public AddProductForm(String name, Long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPrice(Long price) {
        this.price = price;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    //html form의 내용을 binding 하기 위해서는 setter의 존재 필수
    //즉 불변객체가 아닌 가변 객체여야 함
}
