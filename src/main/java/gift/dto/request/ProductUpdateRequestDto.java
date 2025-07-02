package gift.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequestDto(
    @NotBlank
    @Size(max = 15, message = "15자 이내로 입력해주세요")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]{1,15}$")
    String name,
    int price,
    String imageURL) {

}
