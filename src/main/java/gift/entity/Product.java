package gift.entity;

import gift.dto.ProductRequestDto;
import gift.exception.InvalidProductNameException;

public class Product {

    private static final String BLOCKED_PRODUCT_NAME_KEYWORD = "카카오";

    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    public Product(Long id, String name, int price, String imageUrl) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product() {
    }


    public Product(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public static Product from(ProductRequestDto dto) {
        return new Product(dto.name(), dto.price(), dto.imageUrl());
    }

    private void validateName(String name) {
        if (name.contains(BLOCKED_PRODUCT_NAME_KEYWORD)) {
            throw new InvalidProductNameException("상품 이름에 '" + BLOCKED_PRODUCT_NAME_KEYWORD + "'를 포함할 수 없습니다. 담당 MD와 협의 필요");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
