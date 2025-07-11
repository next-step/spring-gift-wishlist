package gift.service;

import gift.dto.response.WishResponseDto;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public void addWish(Long memberId, Long productId, int quantity) {
        boolean exists = productRepository.existsById(productId);
        if (!exists) {
            throw new NoSuchElementException("해당 상품이 존재하지 않습니다.");
        }
        wishRepository.save(memberId, productId, quantity);

    }

    public List<WishResponseDto> getWishes(Long memberId) {
        return wishRepository.findAllByMemberIdWithProduct(memberId);
    }

    public void remove(Long wishId) {
        wishRepository.remove(wishId);
    }
}
