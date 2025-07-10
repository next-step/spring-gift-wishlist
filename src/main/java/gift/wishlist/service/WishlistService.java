package gift.wishlist.service;

import gift.item.Item;
import gift.item.exception.ItemNotFoundException;
import gift.item.repository.ItemRepository;
import gift.wishlist.Wishlist;
import gift.wishlist.dto.WishlistAddDto;
import gift.wishlist.dto.WishlistResponseDto;
import gift.wishlist.repository.WishlistRepository;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ItemRepository itemRepository;

    public WishlistService(WishlistRepository wishlistRepository, ItemRepository itemRepository) {
        this.wishlistRepository = wishlistRepository;
        this.itemRepository = itemRepository;
    }

    public WishlistResponseDto add(Long memberId, @Valid WishlistAddDto wishlistAddDto) {
        // 추가할 상품이 존재하는지 검증
        Item item = itemRepository.findById(wishlistAddDto.itemId())
            .orElseThrow(() -> new ItemNotFoundException(wishlistAddDto.itemId()));

        Wishlist wishlist = new Wishlist(memberId, wishlistAddDto.itemId());
        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        return new WishlistResponseDto(
            savedWishlist.getId(),
            savedWishlist.getMemberId(),
            savedWishlist.getItemId(),
            item.getName(),
            item.getPrice(),
            item.getImageUrl(),
            savedWishlist.getCreatedAt()
        );
    }

    public List<WishlistResponseDto> findAll() {
        List<Wishlist> wishlists = wishlistRepository.findAllByOrderByCreatedAtDesc();

        List<WishlistResponseDto> wishlistResponseDtos = new ArrayList<>();

        for (Wishlist wishlist : wishlists) {
            Item item = itemRepository.findById(wishlist.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(wishlist.getId()));
            WishlistResponseDto dto = new WishlistResponseDto(
                wishlist.getId(),
                wishlist.getMemberId(),
                wishlist.getItemId(),
                item.getName(),
                item.getPrice(),
                item.getImageUrl(),
                wishlist.getCreatedAt()
            );
            wishlistResponseDtos.add(dto);
        }

        return wishlistResponseDtos;

    }
}