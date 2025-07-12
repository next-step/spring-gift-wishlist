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
        return wishRepository.findUserWishes(userInfoRequestDto.id()).stream().map(WishResponseDto::new).collect(Collectors.toList());
    }

    public WishResponseDto addWish(UserInfoRequestDto userInfoRequestDto, WishRequestDto wishRequestDto) {
       return new WishResponseDto(wishRepository.addWish(userInfoRequestDto.id(), new Wish(wishRequestDto)));
    }

    public void updateWish(UserInfoRequestDto userInfoRequestDto, WishRequestDto wishRequestDto) {
        wishRepository.updateWish(userInfoRequestDto.id(), new Wish(wishRequestDto));
    }

    public void deleteWish(UserInfoRequestDto userInfoRequestDto, WishRequestDto wishRequestDto) {
        wishRepository.deleteWish(userInfoRequestDto.id(), new Wish(wishRequestDto));
    }
}