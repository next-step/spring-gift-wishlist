package gift.service;

import gift.dto.WishResponseDto;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public void addWish(Long memberId, Long productId) {
        wishRepository.save(memberId, productId);
    }

    public List<WishResponseDto> getWishes(Long memberId) {
        return wishRepository.findByMemberId(memberId);
    }

    public void removeWish(Long memberId, Long wishId) {
        Long wishOwnerId = wishRepository.findMemberIdByWishId(wishId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 wish 입니다."));

        if (!wishOwnerId.equals(memberId)) {
            throw new IllegalStateException("본인의 wish만 삭제할 수 있습니다.");
        }

        wishRepository.deleteById(wishId);
    }
}
