package gift.entity;

public class Product {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private Boolean validated;

    public Product() {}

    public Product(Long id, String name, Integer price, String imageUrl, Boolean validated) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.validated = validated;
    }

    public Product(String name, Integer price, String imageUrl, Boolean validated) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.validated = validated;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public Boolean getValidated() {
        return validated;
    }
}
