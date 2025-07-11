package gift.service.wish;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import java.util.List;

public interface WishService {

  List<WishResponseDto> findByMemberId(Long memberId);

  WishResponseDto createWish(Long memberId, WishRequestDto requestDto);

  WishResponseDto updateQuantity(Long memberId, WishRequestDto requestDto);

  void deleteByMemberId(Long memberId);
}
