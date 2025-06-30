package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
    @NotBlank
    @Size(max = 255)
    String name,

    @NotNull
    @Min(value = 0)
    Integer price,

    @NotBlank
    @Size(max = 500)
    String imageUrl
) {
    public static ProductRequestDto from() {
        return new ProductRequestDto(
            "",
            0,
            ""
        );
    }
}
