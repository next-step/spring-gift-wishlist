package gift.service;

import gift.dto.WishlistItemRequestDto;
import gift.dto.WishlistItemResponseDto;
import gift.entity.WishlistItem;
import gift.exception.OperationFailedException;
import gift.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public void addWishlistItem(Long memberId, WishlistItemRequestDto requestDto) {
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
