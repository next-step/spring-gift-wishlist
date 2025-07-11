package gift.service;

import gift.dto.wishlist.WishlistResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wishlist;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, MemberRepository memberRepository,
            ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public List<WishlistResponseDto> getWishlists(String userEmail) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<Wishlist> wishlists = wishlistRepository.findAllByMemberId(member.getId());

        return wishlists.stream().map(wishlist -> {
            Product product = productRepository.findById(wishlist.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
            return new WishlistResponseDto(wishlist.getId(), product);
        }).collect(Collectors.toList());
    }

    public void addWishlist(String userEmail, Long productId) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        if (wishlistRepository.existsByMemberIdAndProductId(member.getId(), productId)) {
            throw new IllegalArgumentException("이미 위시리스트에 추가된 상품입니다.");
        }

        wishlistRepository.save(new Wishlist(null, member.getId(), productId));
    }

    public void deleteWishlist(String userEmail, Long wishlistId) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new IllegalArgumentException("위시리스트 아이템을 찾을 수 없습니다."));

        if (!wishlist.getMemberId().equals(member.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        wishlistRepository.deleteById(wishlistId);
    }
}