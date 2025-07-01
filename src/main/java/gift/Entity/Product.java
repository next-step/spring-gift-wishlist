package gift.Entity;

// 검색으로 찾아내어 간편화된 코드
//public record Product(Long id, String name, int price, String imageUrl){}

// 기존 코드

import gift.dto.ProductRequestDto;

public class Product {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    public Product() {

    }

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }

    //setter 추가
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(int price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }



    public void update(ProductRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.imageUrl = requestDto.getImageUrl();
    }
}




