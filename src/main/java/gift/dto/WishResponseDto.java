package gift.dto;

public class WishResponseDto {

    private Long wishId;
    private Long productId;
    private String name;
    private Long price;
    private String imageUrl;

    public WishResponseDto(Long wishId, Long productId, String name, Long price, String imageUrl) {
        this.wishId = wishId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getWishId() {
        return wishId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
