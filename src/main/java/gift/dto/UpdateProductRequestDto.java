package gift.dto;

import gift.validation.ValidProductName;
import gift.validation.ValidProductPrice;

public record UpdateProductRequestDto(
        Long id,

        @ValidProductName
        String name,

        @ValidProductPrice
        Long price,

        String imageUrl,

        boolean isMdApproved
) {
    public UpdateProductRequestDto(Long id, String name, Long price, String imageUrl) {
        this(id, name, price, imageUrl, false);
    }
}
