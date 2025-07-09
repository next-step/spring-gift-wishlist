package gift.service.wishListService;

import gift.dto.wishListDto.AddWishItemDto;
import gift.dto.wishListDto.ResponseWishItemDto;
import gift.entity.WishItem;
import gift.exception.itemException.ItemNotFoundException;
import gift.repository.wishListRepository.WishListRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<ResponseWishItemDto> getItemList(String name, Integer price, String userEmail) {
        List<WishItem> wishItems;
        List<ResponseWishItemDto> result = new ArrayList<>();

        if (name == null && price == null) {
            wishItems = wishListRepository.getAllWishItems(userEmail);
        } else {
            wishItems = wishListRepository.getWishItems(name, price, userEmail);
        }

        if (wishItems.isEmpty()) {
            throw new ItemNotFoundException();
        }

        for (WishItem wishItem : wishItems) {
            result.add(ResponseWishItemDto.from(wishItem));
        }

        return result;
    }
}
