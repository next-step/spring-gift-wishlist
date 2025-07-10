package gift.service;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Product;
import gift.exception.DuplicateWishException;
import gift.exception.ResourceNotFoundException;
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

    public List<WishResponseDto> getWishes(Long memberId){
        return wishRepository.findByMemberId(memberId).stream()
                .map(w -> {
                    Product product = productRepository.findProductById(w.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("상품 정보를 찾을 수 없습니다: ID " + w.getProductId()));

                    return new WishResponseDto(
                            w.getId(),
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getImageUrl()
                    );
                })
                .collect(Collectors.toList());
    }

    public void addWish(Long memberId, WishRequestDto wishRequestDto){
        productRepository.findProductById(wishRequestDto.productId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 상품을 찾을 수 없습니다: " + wishRequestDto.productId()));

        if (wishRepository.existWish(memberId, wishRequestDto.productId())) {
            throw new DuplicateWishException("이미 위시리스트에 추가된 상품입니다.");
        }

        wishRepository.addWish(memberId, wishRequestDto.productId());
    }
}
