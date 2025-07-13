package gift.service;

import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.WishRequest;
import gift.repository.WishRepository;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public List<Product> getWishlist(Long memberId) {
        List<Wish> wishes = wishRepository.findByMemberId(memberId);
        List<Long> productIds = wishes.stream().map(Wish::getProductId).collect(Collectors.toList());
        return productRepository.findAllByIds(productIds);
    }

    public void addWish(Long memberId, WishRequest wishRequest) {
        Wish wish = new Wish(null, memberId, wishRequest.getProductId(), wishRequest.getQuantity());
        wishRepository.save(wish);
    }

    public boolean removeWish(Long memberId, Long productId) {
        return wishRepository.deleteByMemberAndProduct(memberId, productId);
    }

    public void updateWishQuantity(Long memberId, Long wishId, int quantity) {
        Wish wish = wishRepository.findByMemberId(memberId).stream()
            .filter(w -> w.getId().equals(wishId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Wish not found"));
        wish.setQuantity(quantity);
        wishRepository.update(wish);
    }
}
