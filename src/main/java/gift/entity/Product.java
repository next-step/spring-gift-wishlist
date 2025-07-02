package gift.entity;

public class Product {

    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private Boolean approved;

    public Product(Long id, String name, Long price, String imageUrl, boolean approved) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.approved = approved;
    }

    public void updateProduct(String name, Long price, String imageUrl, Boolean approved) {
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

    public boolean getApproved() { return approved; }
}
