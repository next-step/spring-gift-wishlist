package gift.wishlist.service;

import gift.global.exception.CustomException;
import gift.global.exception.ErrorCode;
import gift.item.entity.Item;
import gift.item.repository.ItemRepository;
import gift.member.entity.Member;
import gift.wishlist.dto.WishRequest;
import gift.wishlist.dto.WishResponse;
import gift.wishlist.entity.Wishlist;
import gift.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ItemRepository itemRepository;

    public WishlistService(WishlistRepository wishlistRepository, ItemRepository itemRepository) {
        this.wishlistRepository = wishlistRepository;
        this.itemRepository = itemRepository;
    }

    public List<WishResponse> getWishes(Member member) {
        return wishlistRepository.findByMember(member)
                .stream()
                .map(wish -> new WishResponse(
                        wish.getItem().getId(),
                        wish.getItem().getName(),
                        wish.getItem().getPrice(),
                        wish.getItem().getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    public void addWish(WishRequest request, Member member) {
        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

        if (wishlistRepository.existsByMemberAndItem(member, item)) {
            throw new CustomException(ErrorCode.WISH_ALREADY_EXISTS);
        }

        wishlistRepository.save(new Wishlist(member, item));
    }

    public void deleteWish(Long itemId, Member member) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

        wishlistRepository.deleteByMemberAndItem(member, item);
    }
}
