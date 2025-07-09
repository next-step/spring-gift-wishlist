package gift.product.dto;


import lombok.Builder;


@Builder
public record GetItemResponse(Long id, Long authorId, String name, Integer price, String imageUrl) {
}
