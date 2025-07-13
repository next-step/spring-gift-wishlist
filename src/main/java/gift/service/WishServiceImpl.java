package gift.service;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WishServiceImpl implements WishService {
    private final WishRepository wishRepository;

    public WishServiceImpl(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Override
    public CreateWishResponse create(Long memberId, CreateWishRequest request) {
        Optional<CreateWishResponse> wish = wishRepository.findProductById(memberId, request.productId());
        if (wish.isPresent()) {
            throw new IllegalArgumentException("이미 위시 리스트에 존재하는 상품입니다.");
        }
        System.out.println("위시리스트 추가해야함");
        return wishRepository.saveWish(memberId, request.productId(), request.quantity());
    }
}
