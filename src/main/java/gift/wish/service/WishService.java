package gift.wish.service;

import gift.member.domain.Member;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import gift.wish.domain.Wish;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishResponse;
import gift.wish.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public WishResponse addWish(Member member, Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));

        if (wishRepository.isExist(member.getId(), productId)) {
            throw new IllegalArgumentException("이미 위시 리스트에 추가된 상품입니다.");
        }

        Wish wish = wishRepository.save(member.getId(), productId);

        return new WishResponse(wish.getMemberId(), wish.getProductId(), 1);
    }

    public List<WishListResponse> getWishes(Member member) {
        return wishRepository.findWishes(member.getId());
    }

    public void updateQuantity(Member member, Long wishId, Integer quantity){
        checkValidWishAndMember(member,wishId);

        wishRepository.updateByIdAndQuantity(wishId, quantity);
    }

    public void deleteWish(Member member, Long wishId){
        checkValidWishAndMember(member,wishId);

        wishRepository.deleteById(wishId);
    }

    private void checkValidWishAndMember(Member member, Long wishId){
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new NoSuchElementException("해당 위시 항목을 찾을 수 없습니다."));

        wish.validateOwner(member.getId());
    }
}
