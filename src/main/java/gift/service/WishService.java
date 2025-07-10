package gift.service;

import gift.domain.Wish;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    /**
     * 위시리스트에 상품 추가
     * 이미 찜한 경우 예외 발생
     */
    public Wish addWish(Long memberId, Long productId) {
        if (wishRepository.isWished(memberId, productId)) {
            throw new IllegalStateException("이미 찜한 상품입니다.");
        }
        return wishRepository.addWish(memberId, productId);
    }

    /**
     * 위시리스트에서 상품 제거
     * 멱등성 보장: 존재하지 않아도 예외 없이 삭제 시도
     * 다른 사용자의 찜 항목은 삭제할 수 없음 (wishId + memberId 조건 사용)
     */
    public void removeWish(Long wishId, Long memberId) {
        wishRepository.removeByMemberIdAndWishId(memberId, wishId);
    }

    /**
     * 사용자별 위시리스트 조회
     * 존재하지 않을 경우 빈 리스트 반환
     */
    public List<Wish> getWishlist(Long memberId) {
        return wishRepository.getWishlistByMemberId(memberId);
    }
}
