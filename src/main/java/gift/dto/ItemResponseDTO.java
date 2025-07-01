package gift.dto;

import gift.entity.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ItemResponseDTO(@NotNull Long id,
                              @NotNull @Size(max=15,message = "최대 15자입니다.") @Pattern(regexp = "^[a-zA-Z0-9()\\[\\]+\\-\\&/_]*$", message = "( ), [ ], +, -, &, /, _ 외에는 특수문자는 허용되지 않습니다.") String name,
                              @Min(0) Integer price,
                              @NotNull @Size(max=255)String imageUrl){

    public static ItemResponseDTO from(Item item) {
        return new ItemResponseDTO(item.getId(), item.getName(), item.getPrice(), item.getImageUrl());
    }
}
