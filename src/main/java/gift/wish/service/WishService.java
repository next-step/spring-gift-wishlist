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

    public List<WishResponseDto> getWishlist(WishRequestDto dto) {
        return wishRepository.getWishList(dto).stream()
                .map(WishResponseDto::fromEntity).toList();
    }

    public WishResponseDto addWish(WishRequestDto dto) {
        return wishRepository.addWish(dto);
    }

    public void deleteWish(WishRequestDto dto) {
        wishRepository.deleteWish(dto);
    }

}
