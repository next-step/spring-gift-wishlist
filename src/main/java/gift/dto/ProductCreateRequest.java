package gift.dto;

import gift.entity.Product;
import gift.validation.annotation.KakaoApprovalRequired;
import gift.validation.annotation.ValidProductName;
import gift.validation.group.ValidationGroups;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@KakaoApprovalRequired(
    nameField = "name",
    approvalField = "isMdApproved",
    groups = { ValidationGroups.UserGroup.class}
)
public record ProductCreateRequest(
    @NotNull(message = "상품명은 필수입니다.")
    @ValidProductName
    String name,
    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    Long price,
    @NotNull(message = "이미지 URL은 필수입니다.")
    String imageUrl,
    Boolean isMdApproved
) {

    public ProductCreateRequest(String name, Long price, String imageUrl, Boolean isMdApproved) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isMdApproved = isMdApproved != null ? isMdApproved : false;
    }

    public Product toProduct() {
        return new Product(null, name, price, imageUrl);
    }
}
