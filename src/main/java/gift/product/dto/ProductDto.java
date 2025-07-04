package gift.product.dto;

import gift.common.annotation.BannedWord;
import gift.common.annotation.NoSpecialChar;
import gift.product.domain.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDto {
    @NotNull
    @Size(min = 1, max = 15)
    @NoSpecialChar
    @BannedWord(words = {"카카오"})
    private String name;
    private int price;
    private String imageUrl;

    public ProductDto() {}

    public ProductDto(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
    }

    public ProductDto(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
