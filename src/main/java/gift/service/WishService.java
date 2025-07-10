package gift.service;

import gift.dto.api.WishRequestDto;
import gift.dto.api.WishResponseDto;
import gift.entity.Member;
import gift.entity.WishItem;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository,
                       ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    private WishResponseDto mapWishItemToWishResponseDto(WishItem wi) {
        return new WishResponseDto(
                wi.getProductId(),
                wi.getName(),
                wi.getPrice(),
                wi.getImageUrl(),
                wi.getQuantity()
        );
    }

    public  List<WishResponseDto>getWishListForMember(Member member) {
        List<WishItem> items = wishRepository.findAllByMemberId(member.getId());
        return items.stream()
                .map(this::mapWishItemToWishResponseDto)
                .toList();
    }

    public void addWishItemForMember(Member member, WishRequestDto wishRequestDto) {
        if (productRepository.findById(wishRequestDto.productId()).isEmpty()) {
            throw new NoSuchElementException("상품을 찾을 수 없습니다.");
        }

        wishRepository.updateOrInsertWishItem(
                member.getId(),
                wishRequestDto.productId(),
                wishRequestDto.quantity()
        );
    }
}
