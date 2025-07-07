package gift.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Product {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    @Size(min = 2, max = 15, message = "상품명은 2자 이상 15자 이하로 입력해주세요.")
    private String name;
    private Integer price;
    private String image;
    private Boolean mdApproved = false;

    public Product(Long id, String name, Integer price, String imageUrl, Boolean mdApproved) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = imageUrl;
        this.mdApproved = mdApproved != null ? mdApproved : false;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getPrice() { return price; }
    public String getImage() { return image; }
    public boolean getMdApproved() { return mdApproved; }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setMdApproved(boolean b) { this.mdApproved = b;}

    public void updateFields(Product partialProduct){
        if (partialProduct == null) {
            return;
        }
        if (partialProduct.name != null) {
            this.name = partialProduct.name;
        }
        if (partialProduct.price != null) {
            this.price = partialProduct.price;
        }
        if (partialProduct.image != null) {
            this.image = partialProduct.image;
        }
        if (partialProduct.mdApproved != null) {
            this.mdApproved = partialProduct.mdApproved;
        }

    }

}