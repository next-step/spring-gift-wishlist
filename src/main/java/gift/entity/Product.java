package gift.entity;

import gift.domain.product.ProductName;

public class Product {
    private Long id;
    private ProductName name;
    private Long price;
    private String imageUrl;
    private MdApprovalStatus mdApproval;

    public Product(String name, Long price, String imageUrl) {
        this.name = new ProductName(name);
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(long id, String name, long price, String imageUrl, MdApprovalStatus mdApproved) {
        this.id = id;
        this.name = new ProductName(name);
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdApproval = mdApproved;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
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

    public boolean isApproved() { return mdApproval.isApproved(); }
}
