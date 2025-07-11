package gift.wishproduct.dto;

import gift.domain.WishProduct;

import java.util.UUID;

public class WishProductResponse {

    private UUID id;
    private String productName;
    private int price;
    private int quantity;
    private String imageUrl;

    public WishProductResponse(WishProduct wishProduct) {
        this.id = wishProduct.getId();
        this.productName = wishProduct.getProductName();
        this.price = wishProduct.getPrice();
        this.quantity = wishProduct.getQuantity();
        this.imageUrl = wishProduct.getImageURL();
    }

    protected WishProductResponse() {}

    public UUID getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
