package gift.wishlist.service;

import gift.common.exceptions.WishAlreadyExistsException;
import gift.wishlist.domain.Wishlist;
import gift.wishlist.dto.WishAddRequest;
import gift.wishlist.dto.WishResponse;
import gift.wishlist.repository.WishlistRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Transactional
    public WishResponse addWish(WishAddRequest wishAddRequest, Long memberId) {
        Optional<Wishlist> wishlist =
            wishlistRepository.findByMemberIdAndProductId(
                memberId,
                wishAddRequest.productId()
            );

        if (wishlist.isPresent()) {
            throw new WishAlreadyExistsException("이미 위시리스트에 추가된 상품입니다.");
        }

        return convertToDTO(
            wishlistRepository.save(
                new Wishlist(
                    wishAddRequest.productId(),
                    memberId,
                    wishAddRequest.quantity()
                )
            )
        );
    }

    private WishResponse convertToDTO(Wishlist wishlist) {
        return new WishResponse(
            wishlist.getId(),
            wishlist.getProductId(),
            wishlist.getMemberId(),
            wishlist.getQuantity()
        );
    }
}
