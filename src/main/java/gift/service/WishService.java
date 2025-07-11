package gift.service;

import gift.dto.ProductResponseDto;
import gift.dto.WishResponseDto;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.WishRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductService productService;

    public WishService(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    @Transactional
    public WishResponseDto addWish(Long memberId, Long productId) {
        Product product = productService.findProductOrThrow(productId);

        Wish wish = new Wish(memberId, productId);
        Long id = wishRepository.saveWish(wish);

        return new WishResponseDto(id, ProductResponseDto.from(product));
    }

    @Transactional(readOnly = true)
    public List<WishResponseDto> getWishlist(long memberId) {

        List<Wish> wishes = wishRepository.findAllWishes(memberId);
        return wishes.stream()
                     .map(wish -> {
                         Product product = productService.findProductOrThrow(wish.getProductId());
                         return new WishResponseDto(wish.getId(), ProductResponseDto.from(product));
                     })
                     .toList();
    }

}
