package gift.service;

import gift.dto.ItemResponse;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Item;
import gift.entity.Member;
import gift.entity.Wish;
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

        List<Long> productIds = wishes.stream()
            .map(Wish::getProductId)
            .toList();

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
    public void addWish(WishRequest request, Member member) {
        Wish wish = new Wish(null, member.getId(), request.productId());
        wishRepository.save(wish);
    }

    @Transactional
    public void deleteWish(Long wishId) {
        wishRepository.deleteById(wishId);
    }
}