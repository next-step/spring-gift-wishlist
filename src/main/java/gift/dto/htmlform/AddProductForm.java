package gift.dto.htmlform;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AddProductForm {
    
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]{1,15}$",
        message = "이름은 한글, 영문, 숫자, 공백, (), [], +, -, &, /, _ 만 포함할 수 있으며 최대 15자까지 입력 가능합니다."
    )
    private String name;
    private Long price;
    private String imageUrl;
    
    @NotNull
    private Boolean isMDOK;
    
    public AddProductForm() {
        this(null, null, null, false);
    }
    
    public AddProductForm(String name, Long price, String imageUrl, Boolean isMDOK) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isMDOK = isMDOK;
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
    
    public Boolean getMDOK() {
        return isMDOK;
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
    
    public void setIsMDOK(Boolean isMDOK) {
        this.isMDOK = isMDOK;
    }
}
