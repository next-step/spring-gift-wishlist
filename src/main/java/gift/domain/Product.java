package gift.domain;

public class Product {
    private final Long id;
    private Long categoryId;
    private String name;
    private int price;
    private String imageUrl;

    private Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Product of(Long id, String name, int price, String imageUrl) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("이름은 필수입니다.");
        if (price < 0) throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        if (imageUrl == null || imageUrl.isBlank()) throw new IllegalArgumentException("이미지 URL은 필수입니다.");

        return new Product(id, name, price, imageUrl);
    }

    public void update(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public Long getCategoryId() { return categoryId; }

}
