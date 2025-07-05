package gift.dto;

import gift.entity.Product;

public class ProductResponseDto {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private boolean mdApproved; // MD 협의 여부

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.mdApproved = product.isMdApproved();
    }

    public ProductResponseDto() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public boolean isMdApproved() { return mdApproved; }
}
