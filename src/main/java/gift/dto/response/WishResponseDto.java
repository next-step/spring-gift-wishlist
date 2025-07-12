package gift.dto.response;

import java.util.List;

public record WishResponseDto(
    Long wishId,
    List<String> productList
) {

}
