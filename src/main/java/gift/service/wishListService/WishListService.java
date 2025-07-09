package gift.service.wishListService;

import gift.dto.wishListDto.AddWishItemDto;
import gift.dto.wishListDto.ResponseWishItemDto;
import jakarta.validation.Valid;

public interface WishListService {

    ResponseWishItemDto addWishItem(@Valid AddWishItemDto dto, String userEmail);
}
