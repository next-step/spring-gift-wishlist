package gift.wish.service;

import gift.member.entity.Member;
import gift.wish.dto.WishCreateRequestDto;
import gift.wish.dto.WishCreateResponseDto;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import org.springframework.stereotype.Service;

@Service
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;

    public WishServiceImpl(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Override
    public WishCreateResponseDto addWish(Member member, WishCreateRequestDto wishCreateRequestDto) {

        Wish wish = new Wish(member.getMemberId(), wishCreateRequestDto.productId());
        wishRepository.addWish(wish);

        // TODO: wishes 테이블에서 조회해서 반환하도록 수정 필요
        return new WishCreateResponseDto(wish.getWishId(), wish.getMemberId(), wish.getProductId(),
            wish.getCreateDate());
    }

}
