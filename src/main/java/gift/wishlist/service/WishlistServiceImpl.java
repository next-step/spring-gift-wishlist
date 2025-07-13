package gift.wishlist.service;

import gift.wishlist.dto.CreateWishRequestDto;
import gift.wishlist.dto.UpdateWishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.entity.Wish;
import gift.wishlist.repository.WishlistRepository;
import gift.wishlist.vo.Amount;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceImpl implements WishlistService{
    private final WishlistRepository wishlistRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public List<WishResponseDto> findAllByMemberId(Long memberId) {
        return wishlistRepository.findAllByMemberId(memberId)
            .stream()
            .map(WishResponseDto::from)
            .toList();
    }

    @Override
    public WishResponseDto create(Long memberId, CreateWishRequestDto requestDto) {
        Wish wish = new Wish(
            null,
            memberId,
            requestDto.productId(),
            new Amount(requestDto.amount())
        );
        long id = wishlistRepository.save(wish);

        return WishResponseDto.from(new Wish(id, wish));
    }

    @Override
    public void update(Long id, UpdateWishRequestDto requestDto) {
        wishlistRepository.update(id, new Amount(requestDto.amount()));
    }

    @Override
    public void delete(Long memberId, Long productId) {
        wishlistRepository.delete(memberId, productId);
    }
}
