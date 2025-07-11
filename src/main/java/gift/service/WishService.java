package gift.service;

import gift.dto.WishRequest;
import gift.entity.Wish;
import gift.repository.WishRepository;
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
}
