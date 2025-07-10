package gift.service;

import gift.domain.Wish;
import gift.dto.WishSummaryResponseDto;
import gift.repository.WishListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;

    public WishListService(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public List<WishSummaryResponseDto> findAllWishSummaryByMemberId(Long memberId) {

        return wishListRepository.findAllWishSummaryByMemberId(memberId)
                .stream()
                .map(WishSummaryResponseDto::from)
                .toList();
    }

    public void saveWish(Long memberId, Long productId) {
        wishListRepository.save(new Wish(memberId, productId));
    }
}
