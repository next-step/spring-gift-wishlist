package gift.domain;

import java.util.UUID;

public class WishProduct {

    private UUID id;
    private String productName;
    private int price;
    private int quantity;
    private String imageURL;
    private UUID memberId;
    private UUID productId;


    public WishProduct(UUID id, String productName, int price, int quantity, String imageURL, UUID memberId, UUID productId) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.memberId = memberId;
        this.productId = productId;
    }

    public WishProduct(String productName, int price, int quantity, String imageURL, UUID memberId, UUID productId) {
        this.id = UUID.randomUUID();
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.memberId = memberId;
        this.productId = productId;
    }

    protected WishProduct() {}

    public UUID getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }
}
