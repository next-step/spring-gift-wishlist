package gift.service.wishlist;

import gift.dto.wishlist.WishRequestDto;
import gift.entity.Wish;
import gift.repository.wishlist.WishListRepository;
import org.springframework.stereotype.Service;

@Service
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;

    public WishListServiceImpl(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    @Override
    public void create(WishRequestDto requestDto) {
        Wish wish = wishListRepository.create(
            new Wish(requestDto.productId(), requestDto.memberId()));
    }
}
