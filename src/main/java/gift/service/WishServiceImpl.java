package gift.service;

import gift.dto.ProductResponseDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.wishList.AlreadyInWishListException;
import gift.exception.wishList.WishAccessDeniedException;
import gift.exception.wishList.WishNotFoundException;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishServiceImpl implements WishService{

    private final WishRepository wishRepository;
    private final ProductService productService;

    public WishServiceImpl(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    @Override
    public List<WishResponseDto> findWishList(Long memberId) {
        List<Wish> wishes = wishRepository.findWishList(memberId);

        return wishes.stream()
                .map(wish -> {
                    ProductResponseDto product = productService.findProductByIdElseThrow(wish.getProductId());

                    return new WishResponseDto(
                            wish.getId(),
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getImageUrl()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public WishResponseDto saveWish(Long memberId, WishRequestDto dto) {

        Long productId = dto.getProductId();

        ProductResponseDto product = productService.findProductByIdElseThrow(dto.getProductId());

        if (wishRepository.isInWishList(memberId, productId)) {
            throw new AlreadyInWishListException(
                    "해당 상품은 이미 위시 리스트에 존재합니다."
            );
        }

        Wish wish = wishRepository.saveWish(memberId, dto.getProductId());
        return new WishResponseDto(
                wish.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl());
    }

    @Override
    public void deleteWish(Long wishId, Member member) {

        Wish wish = wishRepository.findWishById(wishId)
                .orElseThrow(() ->
                        new WishNotFoundException(
                                "해당 위시 상품은 존재하지 않습니다."
                        )
                );

        if (!wish.getMemberId().equals(member.getId())) {
            throw new WishAccessDeniedException(
                    "삭제할 권한이 없습니다."
            );
        }

        wishRepository.deleteWish(wishId);
    }
}
