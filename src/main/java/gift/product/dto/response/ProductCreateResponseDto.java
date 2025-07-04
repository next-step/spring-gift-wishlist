package gift.product.dto.response;

public record ProductCreateResponseDto(Long productId,
                                       String name,
                                       Double price,
                                       String imageUrl,
                                       Boolean mdConfirmed) {

}