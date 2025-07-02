package gift.entity;

import gift.dto.CreateProductRequestDto;

public class Product {

    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private Boolean acceptedByMD;

    public Product(Long id, String name, Long price, String imageUrl, Boolean acceptedByMD) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.acceptedByMD = acceptedByMD;

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

    public Boolean getAcceptedByMD() {
        return acceptedByMD;
    }
}
