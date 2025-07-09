package gift.dto;

import gift.domain.Wish;

public class WishResponse {
    private Long id;
    private final Long productId;
    private int quantity;
    private final String name;
    private final int price;
    private final String imageUrl;


    public WishResponse(Long id, Long productId, int quantity, String name, int price, String imageUrl) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static WishResponse from(Wish wish) {
        return new WishResponse(
                wish.getId(),
                wish.getProductId(),
                wish.getQuantity(),
                wish.getName(),
                wish.getPrice(),
                wish.getImageUrl()
        );
    }

    public Long getId() {
        return id;
    }

}

