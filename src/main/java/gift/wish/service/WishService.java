package gift.wish.service;

import gift.member.domain.Member;
import gift.product.domain.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import gift.wish.domain.Wish;
import gift.wish.dto.WishListResponse;
import gift.wish.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public Wish addWish(Member member, Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));

        if (wishRepository.isExist(member.getId(), productId)) {
            throw new IllegalArgumentException("이미 위시 리스트에 추가된 상품입니다.");
        }

        return wishRepository.save(member.getId(), productId);
    }

    public List<WishListResponse> getWishes(Member member) {
        return wishRepository.findByMemberId(member.getId());
    }

    public void updateQuantity(Member member, Long wishId, Integer quantity) throws AccessDeniedException {
        checkValidWishAndMember(member,wishId);

        wishRepository.updateByIdAndQuantity(wishId, quantity);
    }

    public void deleteWish(Member member, Long wishId) throws AccessDeniedException{
        checkValidWishAndMember(member,wishId);

        wishRepository.deleteById(wishId);
    }

    private void checkValidWishAndMember(Member member, Long wishId) throws AccessDeniedException{
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new NoSuchElementException("해당 위시 항목을 찾을 수 없습니다."));

        if (!Objects.equals(wish.getMemberId(), member.getId())){
            throw new AccessDeniedException("해당 위시 항목을 삭제할 권한이 없습니다.");
        }
    }
}
