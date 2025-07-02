package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequestDto(
        @NotBlank(message="상품명은 필수로 입력해야합니다.")
        @Size(max=15, message="상품명은 15자 이내로 입력해야합니다")
        String name,
        Long price,
        String url){
    public ProductUpdateRequestDto(){
        this(null, null, null);
    }
}
