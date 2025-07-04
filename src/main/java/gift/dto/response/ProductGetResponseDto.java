package gift.dto.response;

public record ProductGetResponseDto(Long productId,
                                    String name,
                                    Double price,
                                    String imageUrl,
                                    Boolean mdConfirmed) {

}