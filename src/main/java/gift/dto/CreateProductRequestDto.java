package gift.dto;

import gift.validation.ValidProductName;
import gift.validation.ValidProductPrice;

public record CreateProductRequestDto(
        @ValidProductName
        String name,

        @ValidProductPrice
        Long price,

        String imageUrl
) {
}
