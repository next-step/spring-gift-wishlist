package gift.product.entity;

import gift.product.dto.ProductCreateRequestDto;
import gift.product.dto.ProductUpdateRequestDto;

public class Product {

    private Long id;
    private String name;
    private Long price;
    private String imageUrl;

    public Product() {
    }

    public Product(Long id, ProductCreateRequestDto dto) {
        this.id = id;
        this.name = dto.name();
        this.price = dto.price();
        this.imageUrl = dto.imageUrl();
    }

    public void update(ProductUpdateRequestDto dto) {
        if (dto.name() != null) {
            this.name = dto.name();
        }
        if (dto.price() != null) {
            this.price = dto.price();
        }
        if (dto.imageUrl() != null) {
            this.imageUrl = dto.imageUrl();
        }
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
}
