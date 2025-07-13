package gift.entity;

import gift.dto.ProductRequestDTO;
import gift.dto.ProductResponseDTO;

public class Product {
    private Long id;
    private String name;
    private Long price;
    private String imageUrl;

    public Product() { }

    public Product(String name, Long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void updateFromProductRequestDTO(ProductRequestDTO dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.imageUrl = dto.getImageUrl();
    }
}
