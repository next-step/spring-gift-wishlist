package gift.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateProductRequest(

    @NotEmpty
    @Length(max = 15)
    @Pattern(regexp = "^[0-9a-zA-Z가-힣()\\s\\[\\]+\\-&/_]*$")
    String name,

    @NotNull
    @Min(value = 0)
    Integer price,

    @NotEmpty
    String imageUrl
) {
}
