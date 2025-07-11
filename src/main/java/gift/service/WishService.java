package gift.service;

import gift.dto.response.WishResponseDto;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public void addWish(Long memberId, Long productId, int quantity) {

        wishRepository.save(memberId, productId, quantity);
    }

    public List<WishResponseDto> getWishes(Long memberId) {
        return wishRepository.findAllByMemberIdWithProduct(memberId);
    }

    public void remove(Long wishId) {
        wishRepository.remove(wishId);
    }
}
