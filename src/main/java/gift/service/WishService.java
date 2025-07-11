package gift.service;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.entity.Wish;
import gift.repository.WishRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishService {
    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public void addWish(Long memberId, Long productId){

        if(wishRepository.exists(memberId, productId)) {
            throw new IllegalArgumentException("이미 위시리스트에 추가된 상품입니다.");
        }

        wishRepository.insert(memberId, productId);
    }

    public void removeWish(Long memberId, Long productId){
        wishRepository.delete(memberId, productId);
    }

    public List<Wish> getAllWish(Long memberId){
        return wishRepository.findAllByMemberId(memberId);
    }
}
