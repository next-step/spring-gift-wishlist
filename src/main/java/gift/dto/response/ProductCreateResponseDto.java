package gift.dto.response;

public record ProductCreateResponseDto(Long productId,
                                       String name,
                                       Double price,
                                       String imageUrl,
                                       Boolean mdConfirmed) {

//    public ProductCreateResponseDto(Long productId, String name, Double price, String imageUrl,
//        Boolean mdConfirmed) {
//        this.productId = productId;
//        this.name = name;
//        this.price = price;
//        this.imageUrl = imageUrl;
//        this.mdConfirmed = mdConfirmed;
//    }
}