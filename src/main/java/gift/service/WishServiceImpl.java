package gift.service;

import gift.dto.request.WishAddRequestDto;
import gift.dto.request.WishDeleteRequestDto;
import gift.dto.request.WishUpdateRequestDto;
import gift.dto.response.WishIdResponseDto;
import gift.entity.WishProduct;
import gift.exception.UnauthorizedWishListException;
import gift.repository.UserJdbcRepository;
import gift.repository.WishJdbcRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class WishServiceImpl implements WishService {

    private final UserJdbcRepository userRepository;
    private final WishJdbcRepository wishJdbcRepository;

    public WishServiceImpl(UserJdbcRepository userRepository,
        WishJdbcRepository wishJdbcRepository) {
        this.userRepository = userRepository;
        this.wishJdbcRepository = wishJdbcRepository;
    }

    @Override
    public WishIdResponseDto addProduct(WishAddRequestDto wishAddRequestDto, String email) {
        long wishId = wishJdbcRepository.addProduct(
            wishAddRequestDto.productName(),
            email);
        return new WishIdResponseDto(wishId);
    }

    @Override
    public List<WishProduct> getWishList(String email) {
        return wishJdbcRepository.getWishList(email);
    }

    @Override
    public void deleteProduct(
        String email,
        Long wishId,
        WishDeleteRequestDto wishDeleteRequestDto) {

        if (!Objects.equals(userRepository.findUserIdByEmail(email), wishId)) {
            throw new UnauthorizedWishListException("사용자의 위시 리스트가 아님");
        }
        wishJdbcRepository.deleteProduct(wishId, wishDeleteRequestDto.productName());
    }

    @Override
    public void updateProduct(
        Long wishId,
        String email,
        WishUpdateRequestDto wishUpdateRequestDto) {

        if (!Objects.equals(userRepository.findUserIdByEmail(email), wishId)) {
            throw new UnauthorizedWishListException("사용자의 위시 리스트가 아님");
        }

        wishJdbcRepository.updateWish(
            wishId,
            wishUpdateRequestDto.productName(),
            wishUpdateRequestDto.quantity());
    }
}
