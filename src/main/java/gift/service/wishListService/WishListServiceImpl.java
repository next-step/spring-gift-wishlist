package gift.service.wishListService;

import gift.dto.itemDto.ItemDto;
import gift.dto.wishListDto.AddWishItemDto;
import gift.dto.wishListDto.ResponseWishItemDto;
import gift.entity.Item;
import gift.entity.User;
import gift.entity.WishItem;
import gift.exception.itemException.ItemNotFoundException;
import gift.exception.userException.UserNotFoundException;
import gift.repository.wishListRepository.WishListRepository;
import gift.service.itemService.ItemService;
import gift.service.itemService.ItemServiceImplV2;
import gift.service.userService.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishListServiceImpl implements WishListService{

    private final WishListRepository wishListRepository;
    private final UserService userService;
    private final ItemService itemService;

    public WishListServiceImpl(WishListRepository wishListRepository, UserService userService, ItemService itemService) {
        this.wishListRepository = wishListRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public ResponseWishItemDto addWishItem(AddWishItemDto dto, String userEmail) {
        User user = userService.findUserByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException();
        }

        Item item = itemService.findItemByName(dto.name());
        if (item == null) {
            throw new ItemNotFoundException(dto.name());
        }

        Integer quantity = dto.quantity();
        WishItem addedWishItem = wishListRepository.addWishItem(item.getId(), item.getName(), item.getImageUrl(), item.getPrice(), quantity, user.id());

        /**
         *         Q. 여기서 사실 난 userid itemid quantity 정도만 파라미터로 설정하고
         *            그냥 itemService 비즈니스 로직으로 price, imageurl 채우려고 했는데 이 방식은 너무 불편한가?
         *            좀 파라미터가 과한거 같기도 하고,, 그냥 wish_list table에 imageurl price를 추가하는게 나아보이나?
         */

        return ResponseWishItemDto.from(addedWishItem);
    }

    @Override
    public List<ResponseWishItemDto> getItemList(String name, Integer price, String userEmail) {
        User user = userService.findUserByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException();
        }

        List<WishItem> wishItems = wishListRepository.getAllWishItems(user.id());
        if (wishItems.isEmpty()) {
            throw new ItemNotFoundException();
        }

        List<ResponseWishItemDto> result = new ArrayList<>();

        if (name == null && price == null) {
            for (WishItem wishItem : wishItems) {
                Item item = itemService.findItemById(wishItem.itemId());
                if (item == null) {
                    throw new ItemNotFoundException();
                }

                WishItem addWishItem = itemToWishItem(wishItem, item);
                result.add(ResponseWishItemDto.from(addWishItem));
            }
        } else {
            for (WishItem wishItem : wishItems) {
                Item item = itemService.findItemById(wishItem.itemId());
                if (item == null) {
                    continue;
                }

                if (isValid(item,name,price)) {
                    WishItem addWishItem = itemToWishItem(wishItem, item);
                    result.add(ResponseWishItemDto.from(addWishItem));
                }
            }

            if (result.isEmpty()) {
                throw new ItemNotFoundException();
            }
        }

        return result;
    }
    private boolean isValid(Item item, String name, Integer price) {
        boolean nameMatches = (name == null || item.getName().equals(name));
        boolean priceMatches = (price == null || item.getPrice().equals(price));

        return nameMatches && priceMatches;
    }

    private WishItem itemToWishItem(WishItem base, Item item) {
        return new WishItem(
                base.id(),
                base.itemId(),
                item.getName(),
                item.getImageUrl(),
                item.getPrice(),
                base.quantity()
        );
    }

    @Override
    public ResponseWishItemDto deleteWishItem(String name, String userEmail) {
        WishItem deletedItem = wishListRepository.deleteItem(name, userEmail);

        if (deletedItem == null) {
            throw new ItemNotFoundException();
        }

        return new ResponseWishItemDto(deletedItem.name(),deletedItem.imageUrl(),deletedItem.price(),deletedItem.quantity());
    }

    @Override
    public ResponseWishItemDto updateWishItem(Integer quantity, String name, String userEmail) {
        WishItem updateItem = wishListRepository.updateWishItem(quantity, name, userEmail);

        if (updateItem == null) {
            throw new ItemNotFoundException();
        }

        return new ResponseWishItemDto(updateItem.name(), updateItem.imageUrl(), updateItem.price(), updateItem.quantity());
    }
}
