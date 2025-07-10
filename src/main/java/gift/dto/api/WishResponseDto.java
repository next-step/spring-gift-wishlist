package gift.dto.api;

public record WishResponseDto(
        Long productId,
        String name,
        int price,
        String imageUrl,
        int quantity
) {

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

}
