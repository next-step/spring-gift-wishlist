package gift.dto.api;

import gift.entity.Product;

public class ProductResponseDto {
    
    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    
    public ProductResponseDto() {
    }
    
    public ProductResponseDto(Product product) {
        this(product.id(), product.name(), product.price(), product.imageUrl());
    }
    
    public ProductResponseDto(Long id, String name, Long price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPrice(Long price) {
        this.price = price;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
