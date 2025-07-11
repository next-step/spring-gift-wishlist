package gift.service;

import gift.dto.CreateWishResponseDto;
import gift.dto.WishResponseDto;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.ProductNotExistException;
import gift.exception.WishAlreadyExistException;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository,  ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public List<WishResponseDto> getWishlist(Long memberId) {
        List<Wish> wishes = wishRepository.findByMemberId(memberId);

        return wishes.stream()
                .map(wish -> {
                    Product product = productRepository.findById(wish.getProductId())
                            .orElseThrow(() -> new ProductNotExistException(wish.getProductId()));
                    return new WishResponseDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
                })
                .collect(Collectors.toList());
    }

    public CreateWishResponseDto add(Long memberId, Long productId) {
        if (wishRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw new WishAlreadyExistException(productId);
        }
        Wish wish = wishRepository.save(memberId, productId);
        return new CreateWishResponseDto(wish.getId(), wish.getMemberId(), wish.getProductId());
    }

    public void remove(Long memberId, Long productId) {
        wishRepository.delete(memberId, productId);
    }
}