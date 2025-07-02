package gift.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigInteger;

public record ProductRequestDTO (
        @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다.")
        @Pattern(
                regexp = "^[\\p{L}\\p{N}\\s()\\[\\]+\\-&/_]*$",
                message = "(),[],+,-,&,/,_ 외의 특수 문자는 사용이 불가합니다."
        )
        String name,
        BigInteger price,
        @Size(max = 1000, message = "이미지 URL은 1000자를 초과할 수 없습니다.")
        String imageUrl
) {}
