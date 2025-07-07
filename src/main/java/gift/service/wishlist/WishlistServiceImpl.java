package gift.service.wishlist;

import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishlistInfo;
import gift.repository.member.MemberRepository;
import gift.repository.product.ProductRepository;
import gift.repository.wishlist.WishlistRepository;
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    
    public WishlistServiceImpl(WishlistRepository wishlistRepository,
        ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }
    
    @Override
    public WishlistResponseDto addToMyWishlist(Long userId, WishlistRequestDto requestDto) {
        Member member = memberRepository.findMemberById(userId);
        Product product = productRepository.findProductWithId(requestDto.getProductId());
        
        WishlistInfo wishlistInfo = wishlistRepository.addToMyWishlist(
            member.getId(), product.getId(), requestDto.getProductCnt()
        );
        
        Product addedProduct = productRepository.findProductWithId(wishlistInfo.getProductId());
        
        return new WishlistResponseDto(addedProduct.getName(), requestDto.getProductCnt());
    }
}
