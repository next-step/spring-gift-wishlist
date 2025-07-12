package gift.service;

import gift.dto.WishResponseDto;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.AccessDeniedException;
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

        return WishResponseDto.of(savedWish, product);
    }

    @Override
    public List<WishResponseDto> findAllWishesByMemberId(Long memberId) {
        List<Wish> wishes = wishRepository.findAllWishByMemberId(memberId);

        return wishes.stream()
                .map(wish -> {
                    Product product = productRepository.findProductById(wish.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException(wish.getProductId()));
                    return WishResponseDto.of(wish, product);
                })
                .toList();
    }

    @Override
    public void deleteWish(Long memberId, Long wishId) {
        Wish wish = wishRepository.findWishById(wishId)
                .orElseThrow(() -> new WishNotFoundException(wishId));

        if (!wish.isOwner(memberId)) {
            throw new AccessDeniedException("이 위시를 삭제할 권한이 없습니다.");
        }

        wishRepository.deleteWishById(wishId);
    }
}
