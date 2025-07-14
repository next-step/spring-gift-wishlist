package gift.service;

import gift.domain.WishList;
import gift.dto.WishListRequest;
import gift.dto.WishListResponse;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.repository.WishListRepository;
import gift.repository.projection.WishListWithProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;

    public WishListService(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public List<WishListResponse> getAllWithProductByMemberId(Long memberId) {
        List<WishListWithProduct> wishLists = wishListRepository.findAllWithProductByMemberId(
                memberId);
        return WishListResponse.fromList(wishLists);
    }

    @Transactional
    public WishList createOrUpdate(Long memberId, WishListRequest request) {
        Optional<WishList> existing = wishListRepository.findByMemberIdAndProductId(memberId,
                request.productId());

        return existing
                .map(wishList -> updateExistingWishList(wishList, request.quantity()))
                .orElseGet(() -> createNewWishList(memberId, request));
    }

    @Transactional
    public void delete(Long memberId, Long id) {
        WishList wishList = wishListRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.WISHLIST_NOT_FOUND));

        if (!wishList.memberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        int deletedCount = wishListRepository.deleteByIdAndMemberId(id, memberId);
        if (deletedCount == 0) {
            throw new BusinessException(ErrorCode.WISHLIST_DELETE_FAILED);
        }
    }

    private WishList createNewWishList(Long memberId, WishListRequest request) {
        WishList newWishList = WishList.withoutId(memberId, request.productId(),
                request.quantity());
        return wishListRepository.save(newWishList);
    }

    private WishList updateExistingWishList(WishList existingWishList, int additionalQuantity) {
        existingWishList.addQuantity(additionalQuantity);
        wishListRepository.update(existingWishList.id(), existingWishList);
        return existingWishList;
    }
}
