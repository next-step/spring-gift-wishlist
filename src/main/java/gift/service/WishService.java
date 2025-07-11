package gift.service;

import gift.dto.ProductResponse;
import gift.dto.WishRequest;
import gift.entity.Wish;
import gift.repository.WishRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductService productService;

    public WishService(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    public void addWish(Long memberId, WishRequest request) {
        productService.findProductById(request.productId());

        Wish wish = new Wish(memberId, request.productId());
        wishRepository.save(wish);
    }

    public List<ProductResponse> getWishes(Long memberId) {
        List<Wish> wishes = wishRepository.findByMemberId(memberId);
        return wishes.stream()
                .map(wish -> productService.findProductById(wish.getProductId()))
                .collect(Collectors.toList());
    }

    public void deleteWish(Long memberId, Long productId) {
        boolean deleted = wishRepository.deleteByMemberIdAndProductId(memberId, productId);
        if (!deleted) {
            throw new java.util.NoSuchElementException("해당 상품이 위시리스트에 존재하지 않습니다.");
        }
    }
}
