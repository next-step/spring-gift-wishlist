package gift.dto;

import gift.entity.Product;
import gift.validation.annotation.KakaoApprovalRequired;
import gift.validation.annotation.ValidProductName;
import gift.validation.group.ValidationGroups;
import jakarta.validation.constraints.Min;

@KakaoApprovalRequired(
    nameField = "name",
    approvalField = "isMdApproved",
    groups = { ValidationGroups.UserGroup.class }
)
public record ProductUpdateRequest (
    @ValidProductName
    String name,
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    Long price,
    String imageUrl,
    Boolean isMdApproved
) {
    public ProductUpdateRequest(String name, Long price, String imageUrl, Boolean isMdApproved) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isMdApproved = isMdApproved;
    }

    public Product toEntity(Long productId) {
        return new Product(productId, name, price, imageUrl);
    }
}
