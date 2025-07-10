package gift.wish.service;

import gift.member.entity.Member;
import gift.wish.dto.WishCreateRequestDto;
import gift.wish.dto.WishCreateResponseDto;
import gift.wish.dto.WishGetResponseDto;
import gift.wish.dto.WishPageResponseDto;
import gift.wish.entity.Wish;
import gift.wish.exception.ForbiddenException;
import gift.wish.repository.WishRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;

    public WishServiceImpl(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Override
    public WishCreateResponseDto addWish(Member member, WishCreateRequestDto wishCreateRequestDto) {
        // TODO: 이미 추가한 상품인지 확인하기(WishRepository.existsByMemberAndProduct) 실패 시 예외 처리(이미 존재하는 위시)

        Wish wish = new Wish(member.getMemberId(), wishCreateRequestDto.productId());
        wishRepository.addWish(wish);

        // TODO: wishes 테이블에서 조회해서 반환하도록 수정 필요
        return new WishCreateResponseDto(wish.getWishId(), wish.getMemberId(), wish.getProductId(),
            wish.getCreateDate());
    }

    @Override
    public WishPageResponseDto getWishes(Member member, Integer page, Integer size) {
        int offset = page * size;

        List<Wish> wishes = wishRepository.getWishes(member, size, offset);

        Long total = wishRepository.countWishesByMemberId(member.getMemberId());

        List<WishGetResponseDto> content = wishes.stream()
            .map(wish -> new WishGetResponseDto(
                wish.getWishId(),
                wish.getProductId(),
                wish.getCreateDate()
            ))
            .collect(Collectors.toList());

        Integer totalPages = (int) Math.ceil((double) total / size);

        return new WishPageResponseDto(content, page, size, total, totalPages);
    }

    @Override
    public void deleteWish(Member member, Long wishId) {
        // TODO: 위시 ID 검색(WishRepository.findByWishId), 실패 시 예외 처리(존재하지 않은 위시)
        Wish wish = wishRepository.findByWishId(wishId);

        // TODO: 위시 데이터에 존재하는 회원 ID와 제공한 회원 ID 비교, 실패 시 예외 처리(다른 사용자)
        if (!member.getMemberId().equals(wish.getMemberId())) {
            throw new ForbiddenException("다른 사용자의 위시리스트에 접근할 수 업습니다.");
        }

        wishRepository.deleteWish(wishId);
    }

}
