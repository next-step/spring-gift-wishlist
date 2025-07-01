package gift.dto;

import gift.entity.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemUpdateDTO(@NotNull Long id, @NotNull @Size(max=255) @NotNull String name, @Min(0) Integer price, @NotNull @Size(max=255)String imageUrl) {
    public ItemUpdateDTO(Item item) {
        this(item.getId(), item.getName(), item.getPrice(), item.getImageUrl());
    }
}
