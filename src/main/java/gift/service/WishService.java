package gift.service;

import gift.domain.Product;
import gift.domain.Wish;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public void addWish(Long memberId, Long productId) {
        wishRepository.save(memberId, productId);
    }

    public void removeWish(Long memberId, Long productId) {
        wishRepository.delete(memberId, productId);
    }

    public List<Product> getWishProducts(Long memberId) {
        List<Wish> wishes = wishRepository.findByMemberId(memberId);
        return wishes.stream()
                .map(wish -> productRepository.findById(wish.getProductId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
