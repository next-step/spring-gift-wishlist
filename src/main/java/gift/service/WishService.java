package gift.service;

import gift.dto.UserInfoRequestDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Wish;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public List<WishResponseDto> findUserWishes(UserInfoRequestDto userInfoRequestDto) {
        return wishRepository.findUserWishes(userInfoRequestDto).stream().map(WishResponseDto::new).collect(Collectors.toList());
    }

    public WishResponseDto addWish(WishRequestDto wishRequestDto) {
       return new WishResponseDto(wishRepository.addWish(new Wish(wishRequestDto)));
    }

    public void updateWish(WishRequestDto wishRequestDto) {
        wishRepository.updateWish(new Wish(wishRequestDto));
    }

    public void deleteWish(WishRequestDto wishRequestDto) {
        wishRepository.deleteWish(new Wish(wishRequestDto));
    }
}