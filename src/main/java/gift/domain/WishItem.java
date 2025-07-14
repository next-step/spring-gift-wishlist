package gift.domain;

public class WishItem {

    private final Long id;
    private final Long productId;
    private final Long price;
    private final String name;
    private final String imageUrl;
    private final Long quantity;

    public WishItem(Long id, Long productId, Long price, String name, String imageUrl, Long quantity) {
        this.id = id;
        this.productId = productId;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getQuantity() {
        return quantity;
    }
}
