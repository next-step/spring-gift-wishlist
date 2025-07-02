package gift.dto.response;

public record ProductGetResponseDto(Long productId,
                                    String name,
                                    Double price,
                                    String imageUrl) {

    public ProductGetResponseDto(Long productId, String name, Double price, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}