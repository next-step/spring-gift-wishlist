package gift.entity;

import gift.dto.ProductRequestDto;

public class Product {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private boolean mdApproved; // MD 협의 여부

    public Product() {}

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdApproved = false;
    }

    public Product(Long id, String name, int price, String imageUrl, boolean mdApproved) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdApproved = mdApproved;
    }

    public void update(ProductRequestDto dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.imageUrl = dto.getImageUrl();
        this.mdApproved = dto.isMdApproved();
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isMdApproved() { return mdApproved; }
    public void setMdApproved(boolean mdApproved) { this.mdApproved = mdApproved; }
}
