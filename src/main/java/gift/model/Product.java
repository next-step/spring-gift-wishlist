package gift.model;

import java.time.LocalDateTime;

public class Product {
    private Long id;
    private String name;
    private int price;
    private boolean usableKakao;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(String name, int price, boolean usableKakao, String imageUrl) {
        this.name = name;
        this.price = price;
        this.usableKakao = usableKakao;
        this.imageUrl = imageUrl;
    }
    public Product(String name, int price, boolean usableKakao) {
        this.name = name;
        this.price = price;
        this.usableKakao = usableKakao;
    }
    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    //Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}
    public boolean isUsableKakao() {return usableKakao;}
    public void setUsableKakao(boolean usableKakao) {this.usableKakao = usableKakao;}
    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}

}
