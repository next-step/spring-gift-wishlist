package gift.service;

import gift.dto.api.WishResponseDto;
import gift.entity.Member;
import gift.entity.WishItem;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
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

}
