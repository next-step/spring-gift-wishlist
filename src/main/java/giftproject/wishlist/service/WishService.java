package giftproject.wishlist.service;

import giftproject.gift.dto.ProductResponseDto;
import giftproject.gift.service.ProductService;
import giftproject.wishlist.dto.WishRequestDto;
import giftproject.wishlist.dto.WishResponseDto;
import giftproject.wishlist.entity.Wish;
import giftproject.wishlist.repository.WishRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductService productService;

    public WishService(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    public WishResponseDto save(Long memberId, WishRequestDto requestDto) {
        ProductResponseDto product = productService.findById(requestDto.productId());

        if (wishRepository.findByMemberIdAndProductId(memberId, requestDto.productId())
                .isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 위시 리스트에 추가된 상품입니다.");
        }

        Wish newWish = new Wish(memberId, requestDto.productId());
        Wish savedWish = wishRepository.save(newWish);

        return new WishResponseDto(savedWish, product);
    }

    public List<WishResponseDto> find(Long memberId) {
        List<Wish> wishes = wishRepository.findByMemberId(memberId);

        return wishes.stream()
                .map(wish -> {
                    ProductResponseDto product = productService.findById(wish.getProductId());
                    return new WishResponseDto(wish, product);
                })
                .collect(Collectors.toList());
    }

    public void remove(Long memberId, Long productId) {
        Wish wishToDelete = wishRepository.findByMemberIdAndProductId(memberId, productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "위시 리스트에서 해당 상품을 찾을 수 없습니다."));

        wishRepository.deleteByMemberIdAndProductId(memberId, productId);
    }
}
