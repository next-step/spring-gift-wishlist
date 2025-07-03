package gift.dto;

/*
 ProductRequest와 ProductResponse 현재 동일한 코드 구조를 가지고 있어 하나의 dto로 합쳐도 되지만,
 이후에 기능 확장, 필드 추가 등을 고려해 미리 분리해 놓는 게 낫다고 판단했습니다.
*/

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequest(
    Long id,

    @NotNull(message = "상품명은 필수입니다.")
    @Size(max = 15, message = "상품명은 최대 15자까지 가능합니다.")
    @Pattern(regexp = "^((?!카카오).)*$", message = "'카카오'가 들어간 상품명 등록은 관리자에게 문의해 주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_\\s]+$", message="사용가능한 특수문자: (), [], +, -, &, /, _")
    String name,
    Integer price,
    String imageUrl
) {

    public static ProductRequest createForNewProductForm(){
        return new ProductRequest(null, "new product", 0, "default url");
    }

}
