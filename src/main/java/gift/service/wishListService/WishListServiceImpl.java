package gift.service.wishListService;

import gift.dto.wishListDto.AddWishItemDto;
import gift.dto.wishListDto.ResponseWishItemDto;
import gift.entity.WishItem;
import gift.repository.wishListRepository.WishListRepository;
import org.springframework.stereotype.Service;

@Service
public class WishListServiceImpl implements WishListService{

    private final WishListRepository wishListRepository;

    public WishListServiceImpl(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    @Override
    public ResponseWishItemDto addWishItem(AddWishItemDto dto, String userEmail) {
        String name = dto.name();
        Integer quantity = dto.quantity();

        WishItem addedWishItem = wishListRepository.addWishItem(name, quantity, userEmail);

        return new ResponseWishItemDto(addedWishItem.name(), addedWishItem.imageUrl(), addedWishItem.price(), addedWishItem.quantity());

    }
}
