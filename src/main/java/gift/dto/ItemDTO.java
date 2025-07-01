package gift.dto;

import gift.entity.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ItemDTO {

    @NotNull
    private Long id;

    @NotNull
    @Size(max=15,message = "최대 15자입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9()\\[\\]+\\-\\&/_]*$", message = "( ), [ ], +, -, &, /, _ 외에는 특수문자는 허용되지 않습니다.")
    private String name;

    @NotNull
    @Min(0)
    private Integer price;

    @NotNull
    @Size(max=255)
    private String imageUrl;

    public ItemDTO() {
    }


    public ItemDTO(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.imageUrl = item.getImageUrl();

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
