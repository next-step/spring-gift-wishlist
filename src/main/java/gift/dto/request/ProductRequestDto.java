package gift.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
    long productId,
    String name,
    int price,
    String imageURL) {

}
