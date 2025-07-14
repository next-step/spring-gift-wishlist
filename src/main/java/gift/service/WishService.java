package gift.service;

import gift.dto.ProductResponse;
import gift.dto.WishRequest;
import gift.entity.Wish;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public void addWish(Long memberId, WishRequest request) {
        productRepository.findById(request.productId()).orElseThrow(
                () -> new java.util.NoSuchElementException(
                        "해당 ID의 상품이 존재하지 않습니다: " + request.productId()));

        Wish wish = new Wish(memberId, request.productId());
        wishRepository.save(wish);
    }

    public List<ProductResponse> getWishes(Long memberId) {
        List<Wish> wishes = wishRepository.findByMemberId(memberId);
        if (wishes.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = wishes.stream()
                .map(Wish::getProductId)
                .toList();

        return productRepository.findAllByIdIn(productIds).stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteWish(Long memberId, Long productId) {
        boolean deleted = wishRepository.deleteByMemberIdAndProductId(memberId, productId);
        if (!deleted) {
            throw new java.util.NoSuchElementException("해당 상품이 위시리스트에 존재하지 않습니다.");
        }
    }
}
