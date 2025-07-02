package gift.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public class RequestDto {

    private Long id;

    @NotNull
    @Size(max = 15, message = "공백 포함 최대 15자까지 입력 가능")
    private String name;

    private String imageUrl;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {return imageUrl;}

    public void setId(Long id) {this.id = id;}

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
