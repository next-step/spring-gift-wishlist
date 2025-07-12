package gift.service;

import gift.common.dto.request.AddWishRequest;
import gift.common.dto.response.WishDto;
import gift.common.exception.AuthorityException;
import gift.common.exception.CreationFailException;
import gift.common.exception.EntityNotFoundException;
import gift.domain.member.Member;
import gift.domain.wish.Wish;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public WishDto handleAddWishRequest(Member member, AddWishRequest request) {
        Optional<Wish> found = wishRepository.findByMemberIdProductId(member.getId(), request.productId());
        Wish wish;
        if (found.isEmpty()) {
            wish = createWish(member.getId(), request.productId(), request.quantity());
        } else {
            wish = addWishQuantity(found.get(), request.quantity());
        }
        return WishDto.from(wish);
    }

    public List<WishDto> handleGetMyWishList(Member member) {
        return wishRepository.findAll().stream()
                .filter(w -> w.getMemberId().equals(member.getId()))
                .map(WishDto::from)
                .toList();
    }

    public void handleDeleteWish(Member member, Long wishId) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new EntityNotFoundException("Wish does not exist: id = " + wishId));
        if (!wish.getMemberId().equals(member.getId())) {
            throw new AuthorityException("You don`t have permission to wish:" + wishId);
        }
        wishRepository.delete(wishId);
    }

    private Wish createWish(Long memberId, Long productId, Integer quantity) {
        Wish instance = Wish.of(null, memberId, productId, quantity);
        return wishRepository.save(instance)
                .orElseThrow(() -> new CreationFailException("Fail to create Wish: DB failure"));
    }

    private Wish addWishQuantity(Wish wish, Integer addQuantity) {
        wish.addQuantity(addQuantity);
        return wishRepository.update(wish.getId(), wish)
                .orElseThrow(() -> new EntityNotFoundException("Wish does not exist: id = " + wish.getId()));
    }
}
