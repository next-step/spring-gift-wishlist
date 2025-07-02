package gift.dto;

import gift.entity.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/***
 * resposeDTO에도 굳이 Validation을 적용 시켜야하나?
 * 어차피 반환값일 뿐인데
 */
public record ItemResponseDTO(Long id,
                              String name,
                              Integer price,
                              String imageUrl){

    public static ItemResponseDTO from(Item item) {
        return new ItemResponseDTO(item.getId(), item.getName(), item.getPrice(), item.getImageUrl());
    }
}
