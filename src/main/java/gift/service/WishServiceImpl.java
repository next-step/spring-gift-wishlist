package gift.service;

import gift.dto.WishResponseDto;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.DuplicateWishException;
import gift.exception.ProductNotFoundException;
import gift.exception.WishNotFoundException;
import gift.repository.WishRepository;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishServiceImpl implements WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishServiceImpl(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    @Override
    public WishResponseDto createWish(Long memberId, Long productId) {
        if (wishRepository.existsWishByMemberIdAndProductId(memberId, productId)) {
            throw new DuplicateWishException(memberId, productId);
        }

        Product product = productRepository.findProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Wish savedWish = wishRepository.createWish(new Wish(memberId, productId));

        return WishResponseDto.from(savedWish, product);
    }

    @Override
    public List<WishResponseDto> findAllWishesByMemberId(Long memberId) {
        List<Wish> wishes = wishRepository.findAllWishByMemberId(memberId);

        return wishes.stream()
                .map(wish -> {
                    Product product = productRepository.findProductById(wish.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException(wish.getProductId()));
                    return WishResponseDto.from(wish, product);
                })
                .toList();
    }

    @Override
    public void deleteWishById(Long id) {
        if (!wishRepository.existsWishById(id)) {
            throw new WishNotFoundException(id);
        }

        wishRepository.deleteWishById(id);
    }
}
