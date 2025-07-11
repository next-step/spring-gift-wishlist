package gift.wish.service;

import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import gift.wish.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {

    private final WishRepository wishRepository;
    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    //위시리스트 조회
    public List<WishResponseDto> getWishlist(WishRequestDto dto) {
        return wishRepository.getWishList(dto).stream()
                .map(WishResponseDto::fromEntity).toList();
    }

    //위시리스트 추가
    public WishResponseDto addWish(WishRequestDto dto) {
        return wishRepository.addWish(dto);
    }

    //위시리스트 삭제
    public void deleteWish(WishRequestDto dto) {
        wishRepository.deleteWish(dto);
    }

}
