package gift.service;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Wish;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public List<WishResponseDto> findAllWishes() {
        return wishRepository.findAllWishes().stream().map(WishResponseDto::new).collect(Collectors.toList());
    }

    public WishResponseDto addWish(WishRequestDto wishRequestDto) {
        return new WishResponseDto(wishRepository.addWish(new Wish(wishRequestDto)));
    }

    public WishResponseDto updateWish(WishRequestDto wishRequestDto) {
        return new WishResponseDto(wishRepository.updateWish(new Wish(wishRequestDto)));
    }

    public void deleteWish(WishRequestDto wishRequestDto) {
        wishRepository.deleteWish(new Wish(wishRequestDto));
    }
}