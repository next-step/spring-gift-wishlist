package gift.service;

import gift.dto.WishlistItemRequestDto;
import gift.dto.WishlistItemResponseDto;
import gift.entity.Product;
import gift.entity.WishlistItem;
import gift.exception.OperationFailedException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addWishlistItem(Long memberId, WishlistItemRequestDto requestDto) {
        memberRepository.findMemberByIdOrElseThrow(memberId);
        productRepository.findProductByIdOrElseThrow(requestDto.productId());
        WishlistItem item = new WishlistItem(null, memberId, requestDto.productId(), requestDto.quantity());
        int result = wishlistRepository.addWishlistItem(item);
        if (result == 0) {
            throw new OperationFailedException();
        }
    }

    @Override
    public void deleteWishlistItemById(Long itemId) {
        int result = wishlistRepository.deleteById(itemId);
        if (result == 0) {
            throw new OperationFailedException();
        }
    }

    @Override
    public List<WishlistItemResponseDto> findAllWishlistItemsByMemberId(Long memberId) {
        List<WishlistItem> items = wishlistRepository.findAllWishlistItemsByMemberId(memberId);
        return items.stream()
                .map(item -> new WishlistItemResponseDto(item.id(), item.productId(), item.quantity()))
                .toList();
    }

    @Override
    public void updateWishlistItemById(Long itemId, Long quantity) {
        int result = wishlistRepository.updateWishlistItemById(itemId, quantity);
        if (result == 0) {
            throw new OperationFailedException();
        }
    }
}
