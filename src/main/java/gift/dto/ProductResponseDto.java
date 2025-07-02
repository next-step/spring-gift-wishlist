package gift.dto;

import gift.entity.Product;

public class ProductResponseDto {

    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private Boolean approved;

    public ProductResponseDto(Long id, String name, Long price, String imageUrl, Boolean approved) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.approved = approved;
    }

    public Long getId() {
        return id;
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

    public Boolean getApproved() { return approved; }
}
