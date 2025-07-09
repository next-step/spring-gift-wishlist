package gift.api.service;

import gift.api.domain.Member;
import gift.api.domain.Product;
import gift.api.domain.Wishlist;
import gift.api.dto.WishlistResponseDto;
import gift.api.repository.MemberRepository;
import gift.api.repository.ProductRepository;
import gift.api.repository.WishlistRepository;
import gift.exception.ProductNotFoundException;
import gift.exception.WishlistException;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository,
            MemberRepository memberRepository,
            ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Page<WishlistResponseDto> getWishlist(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Page<Wishlist> wishlistPage = wishlistRepository.findWishlistByMemberId(member.getId(),
                pageable);

        return wishlistPage.map(wishlist -> {
            Product product = productRepository.findProductById(wishlist.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(wishlist.getProductId()));

            return WishlistResponseDto.of(wishlist, product);
        });
    }

    public WishlistResponseDto addProductToWishlist(String email, Long productId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Product product = productRepository.findProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        wishlistRepository.findWishlistByMemberIdAndProductId(member.getId(), productId)
                .ifPresent(wishlist -> {
                    throw new WishlistException("이미 위시리스트에 추가된 상품입니다.");
                });

        Wishlist wishlist = wishlistRepository.save(
                new Wishlist(null, member.getId(), productId, LocalDateTime.now()));

        return WishlistResponseDto.of(wishlist, product);
    }

    public void removeProductFromWishlist(Long wishlistID) {
        boolean deleted = wishlistRepository.deleteWishlist(wishlistID);

        if (!deleted) {
            throw new WishlistException("위시리스트에서 상품 삭제에 실패했습니다.");
        }
    }
}
