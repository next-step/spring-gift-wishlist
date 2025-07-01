package gift.dto.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AddProductRequestDto {
    
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]{1,15}$",
        message = "이름은 한글, 영문, 숫자, 공백, (), [], +, -, &, /, _ 만 포함할 수 있으며 최대 15자까지 입력 가능합니다."
    )
    @NotNull(
        message = "이름은 필수입니다."
    )
    private String name;
    
    @NotNull(
        message = "가격은 필수입니다."
    )
    private Long price;
    
    @NotNull(
        message = "이미지 url은 필수입니다."
    )
    private String imageUrl;
    
    @NotNull(
        message = "MD 협의 여부는 필수입니다."
    )
    private Boolean mdOk;
    
    public AddProductRequestDto() {
        this(null, null, null, null);
    }
    
    public AddProductRequestDto(String name, Long price, String imageUrl, Boolean mdOk) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdOk = mdOk;
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
    
    public Boolean isGoodName() {
        if(name.contains("카카오")) {
            return mdOk;
        }
        return true;
    }
}
