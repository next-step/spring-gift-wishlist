package gift.service;

import gift.dto.request.WishAddRequestDto;
import gift.dto.response.WishResponseDto;
import gift.repository.ProductJdbcRepository;
import gift.repository.UserJdbcRepository;
import gift.repository.WishJdbcRepository;
import org.springframework.stereotype.Service;

@Service
public class WishService implements WishServiceInterface {

    private final UserJdbcRepository userRepository;
    private final ProductJdbcRepository productRepository;
    private final WishJdbcRepository wishJdbcRepository;

    public WishService(UserJdbcRepository userRepository, ProductJdbcRepository productRepository,
        WishJdbcRepository wishJdbcRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.wishJdbcRepository = wishJdbcRepository;
    }

    @Override
    public WishResponseDto addProduct(WishAddRequestDto wishAddRequestDto, String email) {
        long wishId = wishJdbcRepository.addProduct(
            wishAddRequestDto.productName(),
            email);
        return new WishResponseDto(wishId);
    }
}
