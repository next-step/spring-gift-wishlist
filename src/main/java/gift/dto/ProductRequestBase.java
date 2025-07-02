package gift.dto;

import jakarta.validation.constraints.*;

public abstract class ProductRequestBase {
    @NotBlank(message = "상품 이름은 비어 있을 수 없습니다.")
    @Size(max = 15, message = "상품 이름은 공백 포함 최대 15자까지 입력할 수 있습니다.")
    @Pattern(
            regexp = "^[\\p{L}\\p{N}\\s\\(\\)\\[\\]\\+\\-\\&/_]*$",
            message = "상품 이름에는 ( ), [ ], +, -, &, /, _ 의 특수 문자만 사용할 수 있습니다."
    )
    private String name;

    @NotNull(message = "가격은 필수 입력입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Long price;

    private String imageUrl;

    private boolean isMdApproved;

    public ProductRequestBase() {
    }

    public ProductRequestBase(String name, Long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isMdApproved = false;
    }

    public ProductRequestBase(String name, Long price, String imageUrl, boolean isMdApproved) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isMdApproved = isMdApproved;
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

    public boolean getIsMdApproved() {
        return isMdApproved;
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

    public void setIsMdApproved(boolean mdApproved) {
        this.isMdApproved = mdApproved;
    }
}
