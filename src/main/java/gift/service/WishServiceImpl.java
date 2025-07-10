package gift.service;

import gift.dto.CreateWishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Wish;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

@Service
public class WishServiceImpl implements WishService{
    private final WishRepository wishRepository;

    private final ProductService productService;

    public WishServiceImpl(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    @Override
    public WishResponseDto createWish(CreateWishRequestDto requestDto, Long memberId) {
        Wish newWish = new Wish(null,requestDto.productId(), memberId, requestDto.quantity());
        Wish savedWish = wishRepository.createWish(newWish);
        return new WishResponseDto(productService.findProductById(savedWish.getProductId()),
                savedWish.getQuantity());
    }
}