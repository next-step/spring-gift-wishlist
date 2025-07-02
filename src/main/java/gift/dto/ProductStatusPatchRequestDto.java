package gift.dto;

import gift.entity.Product;

public record ProductStatusPatchRequestDto(Product.Status status) {
}
