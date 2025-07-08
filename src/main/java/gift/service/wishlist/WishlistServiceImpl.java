package gift.service.wishlist;

import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishlistInfo;
import gift.repository.member.MemberRepository;
import gift.repository.product.ProductRepository;
import gift.repository.wishlist.WishlistRepository;
import java.util.ArrayList;
import java.util.List;
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
    
    @Override
    public List<WishlistResponseDto> findMyWishlistByUserId(Long userId) {
        Member member = memberRepository.findMemberById(userId);
        List<WishlistInfo> myWishlist = wishlistRepository.findMyWishlistByUserId(member.getId());
        
        List<WishlistResponseDto> responseDtoList = new ArrayList<>();
        for (WishlistInfo info : myWishlist) {
            Product myProduct = productRepository.findProductWithId(info.getProductId());
            WishlistResponseDto dto = new WishlistResponseDto(
                myProduct.getName(),
                info.getProductCnt()
            );
            responseDtoList.add(dto);
        }
        
        return responseDtoList;
    }
    
    @Override
    public void deleteFromMyWishlist(Long userId, Long productId) {
        Member member = memberRepository.findMemberById(userId);
        Product product = productRepository.findProductWithId(productId);
        
        WishlistInfo wishlistInfo = wishlistRepository.checkMyWishlist(member.getId(), product.getId());
        
        wishlistRepository.deleteFromMyWishlist(wishlistInfo.getUserId(), wishlistInfo.getProductId());
    }
    
    @Override
    public WishlistResponseDto modifyProductCntFromMyWishlist(Long userId,
        WishlistRequestDto requestDto) {
        Member member = memberRepository.findMemberById(userId);
        Product product = productRepository.findProductWithId(requestDto.getProductId());
        
        WishlistInfo wishlistInfo = wishlistRepository.checkMyWishlist(member.getId(), product.getId());
        
        WishlistInfo modifiedWishlistInfo = wishlistRepository.modifyProductCntFromMyWishlist(
            wishlistInfo.getUserId(), wishlistInfo.getProductId(), requestDto.getProductCnt()
        );
        
        return new WishlistResponseDto(product.getName(), modifiedWishlistInfo.getProductCnt());
    }
}
