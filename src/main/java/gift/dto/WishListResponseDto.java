package gift.dto;

public class WishListResponseDto {

    private Long id;

    private Long productId;

    private Integer quantity;

    private String name;
    private long price;
    private String imageUrl;

    public WishListResponseDto(Long id, Long productId, Integer quantity, String name, long price, String imageUrl) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId(){
        return id;
    }

    public Long getProductId(){
        return productId;
    }

    public Integer getQuantity(){
        return quantity;
    }
    public String getName(){
        return name;
    }
    public long getPrice(){

        return price;
    }
    public String getImageUrl(){
        return imageUrl;
    }


}
