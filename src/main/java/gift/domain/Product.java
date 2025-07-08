package gift.domain;

public class Product {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    private Product() {}

    private Product(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Product of(Long id, String name, Integer price, String imageUrl) {
        return new Product(id, name, price, imageUrl);
    }

    public static Product of(String name, Integer price, String imageUrl) {
        return new Product(null, name, price, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
