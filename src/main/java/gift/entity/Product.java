package gift.entity;

public class Product {

    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private boolean mdApproved;

    public Product(Long id, String name, Long price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdApproved = false;
    }

    public Product(String name, Long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(long id, String name, long price, String imageUrl, boolean mdApproved) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdApproved = mdApproved;
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

    public void setId(Long productId) {
        this.id = productId;
    }

    public boolean getMdApproved() { return mdApproved; }
}
