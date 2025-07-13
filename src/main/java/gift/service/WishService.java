package gift.service;

import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Item;
import gift.entity.Member;
import gift.entity.Wish;
import gift.exception.AuthorizationException;
import gift.exception.ItemNotFoundException;
import gift.repository.ItemRepository;
import gift.repository.WishRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ItemRepository itemRepository;

    public WishService(WishRepository wishRepository, ItemRepository itemRepository) {
        this.wishRepository = wishRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public List<WishResponse> getWishes(Member member) {
        List<Wish> wishes = wishRepository.findByMemberId(member.getId());
        if (wishes.isEmpty()) {
            return List.of();
        }
        List<Long> productIds = wishes.stream().map(Wish::getProductId).toList();
        Map<Long, Item> productsMap = itemRepository.findAllByIdIn(productIds).stream()
            .collect(Collectors.toMap(Item::getId, Function.identity()));

        return wishes.stream()
            .map(wish -> {
                Item item = productsMap.get(wish.getProductId());
                if (item == null) {
                    return null;
                }
                return WishResponse.from(wish, item);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @Transactional
    public WishResponse addWish(WishRequest request, Member member) {
        Wish newWish = new Wish(null, member.getId(), request.productId(), request.quantity());
        Wish savedWish = wishRepository.save(newWish);

        Item item = itemRepository.findById(savedWish.getProductId())
            .orElseThrow(() -> new ItemNotFoundException("상품 정보를 찾을 수 없습니다."));
        return WishResponse.from(savedWish, item);
    }

    @Transactional
    public void updateWishQuantity(Long wishId, int quantity, Member loginMember) {
        Wish wish = wishRepository.findById(wishId)
            .orElseThrow(() -> new ItemNotFoundException("수정할 위시 항목을 찾을 수 없습니다."));

        if (!wish.getMemberId().equals(loginMember.getId())) {
            throw new AuthorizationException("자신의 위시리스트만 수정할 수 있습니다.");
        }

        wish.setQuantity(quantity);
        wishRepository.update(wish);
    }

    @Transactional
    public void deleteWish(Long wishId, Member loginMember) {
        Wish wish = wishRepository.findById(wishId)
            .orElseThrow(() -> new ItemNotFoundException("삭제할 위시 항목을 찾을 수 없습니다."));

        if (!wish.getMemberId().equals(loginMember.getId())) {
            throw new AuthorizationException("자신의 위시리스트만 삭제할 수 있습니다.");
        }

        wishRepository.deleteById(wishId);
    }
}