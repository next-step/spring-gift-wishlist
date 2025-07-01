package gift.dto;

import gift.entity.Item;
import gift.validation.UseKakaoValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@UseKakaoValid
public record ItemUpdateDTO(@NotNull Long id,
                            String name,
                            @Min(0) Integer price,
                            @NotNull @Size(max=255)String imageUrl,
                            boolean useKakaoName) {
    public ItemUpdateDTO(Item item) {
        this(item.getId(), item.getName(), item.getPrice(), item.getImageUrl(),false);
    }
}
