package gift.product.entity;

import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;
    private BigDecimal price;
    private String imgUrl;

    public Product() {}

    public Product(Long id, String name, BigDecimal price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }


    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public void updateWith(ProductRequest request) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.imgUrl = request.getImgUrl();
    }
    public ProductResponse toResponse() {
        return new ProductResponse(id, name, price, imgUrl);
    }
}
