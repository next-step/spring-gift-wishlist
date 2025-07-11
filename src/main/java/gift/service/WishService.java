package gift.service;

import gift.domain.Product;
import gift.domain.Wish;
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
        return wishes.stream()
                .map(wish -> productRepository.findById(wish.getProductId()).orElse(null))
                .filter(p -> p != null)
                .collect(Collectors.toList());
    }

    public void addWish(Long memberId, Long productId) {
        wishRepository.save(new Wish(null, memberId, productId));
    }

    public boolean removeWish(Long memberId, Long productId) {
        return wishRepository.deleteByMemberAndProduct(memberId, productId);
    }
}
