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

    public Product updateId(Long id) {
        return new Product(id, name, price, imageUrl, validated);
    }

    public Long getId() {
        return id;
    }

    public Product updateName(String name) {
        return new Product(id, name, price, imageUrl, validated);
    }

    public String getName() {
        return name;
    }

    public Product updatePrice(Integer price) {
        return new Product(id, name, price, imageUrl, validated);
    }

    public Integer getPrice() {
        return price;
    }

    public Product updateImageUrl(String imageUrl) {
        return new Product(id, name, price, imageUrl, validated);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Product updateValidated(Boolean validated) {
        return new Product(id, name, price, imageUrl, validated);
    }

    public Boolean getValidated() {
        return validated;
    }
}
