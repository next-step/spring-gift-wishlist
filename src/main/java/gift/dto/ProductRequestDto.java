package gift.dto;

import gift.validation.ValidProduct;

@ValidProduct
public class ProductRequestDto {
    private String name;
    private int price;
    private String imageUrl;
    private boolean mdApproved; // MD 협의 여부

    public ProductRequestDto() {}

    public ProductRequestDto(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdApproved = false;
    }

    public ProductRequestDto(String name, int price, String imageUrl, boolean mdApproved) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdApproved = mdApproved;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }

    public void setName(String name) { this.name = name; }
    public void setPrice(int price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl;}
    public boolean isMdApproved() { return mdApproved; }
    public void setMdApproved(boolean mdApproved) { this.mdApproved = mdApproved; }

}
