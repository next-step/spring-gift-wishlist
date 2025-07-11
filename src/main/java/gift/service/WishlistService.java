package gift.service;

import gift.dto.LoginMemberDto;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.model.Wishlist;
import gift.repository.WishlistRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final ProductService productService;
    private final MemberService memberService;
    private final WishlistRepository wishlistRepository;

    public WishlistService(ProductService productService, MemberService memberService,
        WishlistRepository wishlistRepository) {
        this.productService = productService;
        this.memberService = memberService;
        this.wishlistRepository = wishlistRepository;
    }

    public WishResponse create(WishRequest request, LoginMemberDto memberDto) {
        Long memberId = memberService.findByEmail(memberDto.getEmail()).getId();
        Long productId = productService.getProduct(request.getProductId()).getId();

        Optional<Wishlist> existingWish = wishlistRepository.findByMemberIdAndProductId(memberId, productId);
        if (existingWish.isPresent()) {
            throw new IllegalStateException("해당 상품은 이미 위시리스트에 존재합니다.");
        }

        Wishlist wishlist = new Wishlist(memberId, productId, request.getQuantity());
        Wishlist saved = wishlistRepository.save(wishlist);

        return new WishResponse(saved.getMemberId(), saved.getProductId(), saved.getQuantity());
    }

}
